package com.openclassrooms.realestatemanager.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    fun isCurrencyEuro(): Flow<Boolean>
    suspend fun toggleCurrency()
}
