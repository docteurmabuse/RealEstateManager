package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.property.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun addProperty(property: Property)

    suspend fun searchProperties(
        query: String,
        type: List<String>?,
        museum: Boolean?,
        schools: Boolean?,
        shops: Boolean?,
        hospital: Boolean?,
        stations: Boolean?,
        park: Boolean?
    ): Flow<List<Property>>

    suspend fun getAllProperties(): Flow<List<Property>>

    fun getPropertyTypes(): List<Property.PropertyType>

    suspend fun getPropertyById(id: String): Flow<Property>

    suspend fun updateProperty(property: Property)
}
