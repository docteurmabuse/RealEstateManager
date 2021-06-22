package com.openclassrooms.realestatemanager.prefsstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.openclassrooms.realestatemanager.prefsstore.PrefsStoreImpl.PreferenceKeys.IS_CURRENCY_EURO
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val STORE_NAME = "real_estate_data_store"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

class PrefsStoreImpl @Inject constructor(
    @ApplicationContext context: Context
) : PrefsStore {
    private val settingsDataStore = context.dataStore

    override fun isCurrencyEuro() = settingsDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[IS_CURRENCY_EURO] ?: false
    }

    override suspend fun toggleCurrency() {
        settingsDataStore.edit {
            it[IS_CURRENCY_EURO] = !(it[IS_CURRENCY_EURO] ?: false)
        }
    }

    private object PreferenceKeys {
        val IS_CURRENCY_EURO = booleanPreferencesKey("is_currency_euro")
    }
}

