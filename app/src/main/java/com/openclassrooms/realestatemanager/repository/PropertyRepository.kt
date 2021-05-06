package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun addProperty(property: Property)

    suspend fun searchProperty(query: String): List<Property>

    suspend fun getAllProperties(): Flow<List<Property>>

    fun getPropertyTypes(): List<Property.Type>

    suspend fun getPropertyById(id: Int): Property

    suspend fun updateProperty(property: Property)
}