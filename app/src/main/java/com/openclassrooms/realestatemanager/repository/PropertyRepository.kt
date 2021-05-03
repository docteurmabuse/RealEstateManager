package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.Property

interface PropertyRepository {
    suspend fun addProperty(property: Property)

    suspend fun searchProperty(query: String): List<Property>

    suspend fun getAllProperties(): List<Property>

    suspend fun getPropertyById(id: Int): Property

    suspend fun updateProperty(property: Property)
}