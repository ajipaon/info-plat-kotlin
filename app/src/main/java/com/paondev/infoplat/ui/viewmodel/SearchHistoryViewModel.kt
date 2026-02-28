package com.paondev.infoplat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.locale.InfoPlatDao
import com.paondev.infoplat.model.History
import com.paondev.infoplat.ui.screen.RecentSearch
import com.paondev.infoplat.ui.screen.VehicleStatus
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
    val isMotorcycle: Boolean = false
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
                        val response = Gson().fromJson(history.data, JabarPajakResponse::class.java)
                        
                        // Extract vehicle model from response data
                        val model = response.data?.namaModel ?: "Unknown"
                        val jenis = response.data?.jenis ?: ""
                        
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
                            isMotorcycle = isMotorcycle
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
                        val response = Gson().fromJson(displayItem.responseData, JabarPajakResponse::class.java)
                        val status = if (response.data?.canBePaid == false) VehicleStatus.CLEAN else VehicleStatus.TAX_DUE
                        
                        RecentSearch(
                            plate = displayItem.plate,
                            carModel = "${response.data?.tahunBuatan ?: ""} ${displayItem.model}".trim(),
                            status = status,
                            statusLabel = if (status == VehicleStatus.CLEAN) "Paid" else "Due"
                        )
                    }
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