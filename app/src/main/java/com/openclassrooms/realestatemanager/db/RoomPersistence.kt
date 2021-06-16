package com.openclassrooms.realestatemanager.db

import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
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

    override suspend fun getAgentById(agentId: String): Flow<AgentEntity> {
        return agentDao.getAgentById(agentId)
    }

    override suspend fun getAllProperties(): Flow<List<PropertyEntityAggregate>> {
        return propertyDao.getAllProperties()
    }

    override suspend fun searchProperties(
        searchQuery: String
    ): Flow<List<PropertyEntityAggregate>> {
        return propertyDao.searchProperties(
            searchQuery
        )
    }

    override suspend fun filterSearchProperties(
        textQuery: String?,
        museum: Int?,
        school: Int?,
        shop: Int?,
        hospital: Int?,
        station: Int?,
        park: Int?,
        area: String?,
        typeList: List<String?>?,
        minSurface: Float?,
        maxSurface: Float?,
        minPrice: Float?,
        maxPrice: Float?,
        sold: Int?,
        sellDate: Long?,
        soldDate: Long?,
        numberOfPics: Float?,
        rooms: Float?,
        beds: Float?,
        baths: Float?,
        sortBy: String?
    ): Flow<List<PropertyEntityAggregate>> {
        Timber.d("FILTER_SEARCH  $park,  $hospital, $area, $shop, $school, $museum, $museum , $typeList ")

        return propertyDao.filterSearchProperties(
            textQuery = textQuery,
            museum = museum,
            school = school,
            shop = shop,
            hospital = hospital,
            station = station,
            park = park,
            area = area,
            typeList = typeList,
            minSurface = minSurface,
            maxSurface = maxSurface,
            minPrice,
            maxPrice,
            sold,
            sellDate,
            soldDate,
            numberOfPics,
            rooms,
            beds,
            baths,
            sortBy
        )
    }

    override suspend fun storeProperty(
        property: PropertyEntityAggregate,
    ) {
        return propertyDao.insertProperty(property)
    }

    override suspend fun getPropertyById(propertyId: String): Flow<PropertyEntityAggregate> {
        return propertyDao.getPropertyById(propertyId)
    }

    override suspend fun updateProperty(property: PropertyEntityAggregate) {
        return propertyDao.updateProperty(property)
    }
}
