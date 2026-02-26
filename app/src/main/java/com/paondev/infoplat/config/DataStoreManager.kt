package com.paondev.infoplat.config

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

class DataStoreManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

        val His = booleanPreferencesKey("is_dark_mode")

    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_DARK_MODE] = enabled
        }
    }

}

val Context.dataStore by preferencesDataStore(name = "settings")