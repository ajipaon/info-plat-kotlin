package com.paondev.infoplat.config

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val SELECTED_PROVINCE_CODE = stringPreferencesKey("selected_province_code")
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_DARK_MODE] = enabled
        }
    }

    suspend fun saveSelectedProvince(code: String) {
        dataStore.edit { prefs ->
            prefs[SELECTED_PROVINCE_CODE] = code
        }
    }

    fun getSelectedProvince(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[SELECTED_PROVINCE_CODE]
        }
    }

    fun getDarkMode(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[IS_DARK_MODE] ?: false
        }
    }

}

val Context.dataStore by preferencesDataStore(name = "settings")