package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyWithAgentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomPersistence @Inject constructor(
    private val propertyDao: PropertyDao,
    private val agentDao: AgentDao
) : Persistence {

    override suspend fun storeAgent(agent: AgentEntity): Long {
        return agentDao.insertAgent(agent)
    }

    override suspend fun getAllAgents(): Flow<List<AgentEntity>> {
        return agentDao.getAllAgents()
    }

    override suspend fun getAllProperties(): Flow<List<PropertyWithAgentEntity>> {
        return propertyDao.getAllProperties()
    }

    override suspend fun storeProperty(
        property: PropertyWithAgentEntity,
    ) {
        return propertyDao.insertProperty(property)
    }

    override suspend fun getPropertyById(propertyId: String): PropertyWithAgentEntity {
        return propertyDao.getPropertyById(propertyId)
    }

    override suspend fun updateProperty(property: PropertyWithAgentEntity) {
        return propertyDao.updateProperty(property)
    }

}
