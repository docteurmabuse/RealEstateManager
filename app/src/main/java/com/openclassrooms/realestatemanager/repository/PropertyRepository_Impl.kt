package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityMapper
import com.openclassrooms.realestatemanager.domain.model.Property

class PropertyRepository_Impl(
    private val propertyEntityMapper: PropertyEntityMapper,
) : PropertyRepository {
    override suspend fun addProperty(property: Property) {

    }

    override suspend fun searchProperty(query: String): List<Property> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProperties(): List<Property> {
        TODO("Not yet implemented")
    }

    override suspend fun getPropertyById(id: Int): Property {
        TODO("Not yet implemented")
    }

    override suspend fun updateProperty(property: Property) {
        TODO("Not yet implemented")
    }


}