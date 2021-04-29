package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.Property

interface PropertyRepository {
    suspend fun search(query: String): List<Property>
    suspend fun get(id: Int): Property
}