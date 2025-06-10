package com.chrwoods.mtawatchwidget.complication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "Complications",
)

/**
 * Returns the current state for a given complication.
 */
suspend fun ComplicationToggleArgs.getState(context: Context): Long {
    val stateKey = getStatePreferenceKey()
    return context.dataStore.data
        .map { preferences ->
            preferences[stateKey] ?: 0
        }
        .first()
}

/**
 * Updates the current state for a given complication, incrementing it by 1.
 */
suspend fun ComplicationToggleArgs.updateState(context: Context) {
    val stateKey = getStatePreferenceKey()
    context.dataStore.edit { preferences ->
        val currentValue = preferences[stateKey] ?: 0
        // benign overflow possible, all samples take a modulo of this number
        preferences[stateKey] = currentValue + 1
    }
}