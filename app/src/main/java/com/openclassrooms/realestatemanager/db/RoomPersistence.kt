package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomPersistence @Inject constructor(
    private val propertyDao: PropertyDao,
    private val agentDao: AgentDao
) : Persistence {

    override suspend fun storeAgent(agent: AgentEntity): Long {
        return agentDao.insertAgent(agent)
    }

    override suspend fun getAllProperties(): Flow<List<PropertyEntityAggregate>> {
        return propertyDao.getAllProperties()
    }

    override suspend fun storeProperty(
        property: PropertyEntityAggregate,
    ) {
        return propertyDao.insertProperty(property)
    }

    override suspend fun getPropertyById(propertyId: Int): PropertyEntityAggregate {
        return propertyDao.getPropertyById(propertyId)
    }

    override suspend fun updateProperty(property: PropertyEntityAggregate) {
        return propertyDao.updateProperty(property)
    }

}