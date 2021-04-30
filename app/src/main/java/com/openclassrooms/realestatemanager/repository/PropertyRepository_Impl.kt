package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.Property

class PropertyRepository_Impl : PropertyRepository {

    override suspend fun search(query: String): List<Property> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Int): Property {
        TODO("Not yet implemented")
    }

}