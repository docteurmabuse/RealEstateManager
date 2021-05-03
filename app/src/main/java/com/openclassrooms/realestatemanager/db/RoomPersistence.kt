package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity
import javax.inject.Inject

class RoomPersistence @Inject constructor(
    private val propertyDao: PropertyDao,
    private val agentDao: AgentDao
) : Persistence {
    override suspend fun getAllProperties(): List<PropertyEntityAggregate> {
        return propertyDao.getAllProperties()
    }

    override suspend fun storeAgent(agent: AgentEntity): Long {
        return agentDao.insertAgent(agent)
    }

    override suspend fun storeProperty(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>
    ): LongArray {
        return propertyDao.insertPropertyAggregate(property, photos, videos)
    }

    override suspend fun getPropertyById(): PropertyEntityAggregate {
        TODO("Not yet implemented")
    }

    override suspend fun updateProperty(property: PropertyEntityAggregate) {
        TODO("Not yet implemented")
    }

}