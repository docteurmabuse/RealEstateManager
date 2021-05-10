package com.openclassrooms.realestatemanager.interactors.property

import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityMapper
import com.openclassrooms.realestatemanager.domain.model.Property
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchProperties(
    private val propertyDao: PropertyDao,
    private val propertyEntityMapper: PropertyEntityMapper
) {
    fun execute(query: String): Flow<DataState<List<Property>>> = flow {
        try {
            emit(DataState.loading())

            // force error for testing
            if (query == "error") {
                throw Exception("Search FAILED!")
            }

            // query Room database
            val propertyResult = if (query.isBlank()) {
                propertyDao.getAllProperties()
            } else {
                propertyDao.searchProperties(query = query)
            }

            //emit LIst<Property> from Room
            val list =
                propertyEntityMapper.fromPropertyEntityList(propertyResult as List<PropertyEntity>)

            emit(DataState.success(list))
        } catch (e: Exception) {
            emit(DataState.error<List<Property>>(e.message ?: "Unknown Error"))
        }
    }
}