package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.domain.model.search.SearchFilters
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun addProperty(property: Property)

    suspend fun searchProperties(
        query: String
    ): Flow<List<Property>>

    suspend fun filterSearchProperties(
        query: SearchFilters, sortBy: String
    ): Flow<List<Property>>

    suspend fun getAllProperties(): Flow<List<Property>>

    fun getPropertyTypes(): List<Property.PropertyType>

    suspend fun getPropertyById(id: String): Flow<Property>

    suspend fun updateProperty(property: Property)
}
