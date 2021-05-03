package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity

interface Persistence {
    suspend fun getAllProperties(): List<PropertyEntityAggregate>

    suspend fun getPropertyById(): PropertyEntityAggregate

    suspend fun storeAgent(agent: AgentEntity): Long

    suspend fun storeProperty(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>
    ): LongArray

    suspend fun updateProperty(property: PropertyEntityAggregate)
}