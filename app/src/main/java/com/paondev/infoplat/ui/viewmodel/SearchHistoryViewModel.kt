package com.paondev.infoplat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.data.locale.InfoPlatDao
import com.paondev.infoplat.model.RecentSearch
import com.paondev.infoplat.model.VehicleStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HistoryDisplayItem(
    val id: Long,
    val plate: String,
    val model: String,
    val date: String,
    val requestDate: Date,
    val responseData: String,
    val isMotorcycle: Boolean = false,
    val region: String = ""
)

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val dao: InfoPlatDao
) : ViewModel() {

    private val _historyItems = MutableStateFlow<List<HistoryDisplayItem>>(emptyList())
    val historyItems: StateFlow<List<HistoryDisplayItem>> = _historyItems.asStateFlow()

    private val _recentHistory = MutableStateFlow<List<RecentSearch>>(emptyList())
    val recentHistory: StateFlow<List<RecentSearch>> = _recentHistory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            dao.getAllHistory().collect { historyList ->
                val displayItems = historyList.mapNotNull { history ->
                    try {
                        // Parse data based on region
                        val jabarResponse: JabarPajakResponse? = when (history.region) {
                            "JTM" -> {
                                // Parse JTM data and convert to Jabar format
                                try {
                                    val jatimResponse = Gson().fromJson(history.data, JatimPkbResponse::class.java)
                                    com.paondev.infoplat.ui.screen.convertJatimToJabar(jatimResponse)
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            else -> {
                                // Parse as Jabar response (default)
                                Gson().fromJson(history.data, JabarPajakResponse::class.java)
                            }
                        }
                        
                        if (jabarResponse == null) return@mapNotNull null
                        
                        // Extract vehicle model from response data
                        val model = jabarResponse.data?.namaModel ?: "Unknown"
                        val jenis = jabarResponse.data?.jenis ?: ""
                        
                        // Determine if it's a motorcycle
                        val isMotorcycle = jenis.lowercase().contains("motor") || 
                                         jenis.lowercase().contains("sepeda") ||
                                         model.lowercase().contains("motor") ||
                                         model.lowercase().contains("sepeda")
                        
                        HistoryDisplayItem(
                            id = history.id,
                            plate = history.code,
                            model = "$model $jenis".trim(),
                            date = dateFormatter.format(history.requestDate),
                            requestDate = history.requestDate,
                            responseData = history.data,
                            isMotorcycle = isMotorcycle,
                            region = history.region
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _historyItems.value = displayItems

                // Update recent history (max 2, sorted by date descending)
                val recentItems = displayItems
                    .sortedByDescending { it.requestDate }
                    .take(2)
                    .map { displayItem ->
                        val response: JabarPajakResponse? = try {
                            // Try parsing based on region
                            when (displayItem.region) {
                                "JTM" -> {
                                    val jatimResponse = Gson().fromJson(displayItem.responseData, JatimPkbResponse::class.java)
                                    com.paondev.infoplat.ui.screen.convertJatimToJabar(jatimResponse)
                                }
                                else -> {
                                    Gson().fromJson(displayItem.responseData, JabarPajakResponse::class.java)
                                }
                            }
                        } catch (e: Exception) {
                            null
                        }
                        
                        if (response == null) return@map null
                        
                        val status = if (response.data?.canBePaid == false) VehicleStatus.CLEAN else VehicleStatus.TAX_DUE
                        
                        RecentSearch(
                            plate = displayItem.plate,
                            carModel = "${response.data?.tahunBuatan ?: ""} ${displayItem.model}".trim(),
                            status = status,
                            statusLabel = if (status == VehicleStatus.CLEAN) "Paid" else "Due"
                        )
                    }.filterNotNull()
                _recentHistory.value = recentItems
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            dao.deleteAllHistory()
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            dao.deleteHistoryById(id)
        }
    }
}