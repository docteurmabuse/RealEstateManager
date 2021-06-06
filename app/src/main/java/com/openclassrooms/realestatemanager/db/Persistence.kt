package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import kotlinx.coroutines.flow.Flow

interface Persistence {
    suspend fun storeAgent(agent: AgentEntity): Long

    suspend fun getAllAgents(): Flow<List<AgentEntity>>

    suspend fun getAgentById(agentId: String): Flow<AgentEntity>

    suspend fun getAllProperties(): Flow<List<PropertyEntityAggregate>>

    suspend fun searchProperties(searchQuery: String): Flow<List<PropertyEntityAggregate>>

    suspend fun getPropertyById(propertyId: String): Flow<PropertyEntityAggregate>

    suspend fun storeProperty(
        property: PropertyEntityAggregate
    )

    suspend fun updateProperty(property: PropertyEntityAggregate)
}
