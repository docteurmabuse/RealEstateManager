package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyWithAgentEntity
import kotlinx.coroutines.flow.Flow

interface Persistence {
    suspend fun storeAgent(agent: AgentEntity): Long

    suspend fun getAllAgents(): Flow<List<AgentEntity>>

    suspend fun getAllProperties(): Flow<List<PropertyWithAgentEntity>>

    suspend fun getPropertyById(propertyId: Int): PropertyWithAgentEntity

    suspend fun storeProperty(
        property: PropertyWithAgentEntity
    )

    suspend fun updateProperty(property: PropertyWithAgentEntity)
}
