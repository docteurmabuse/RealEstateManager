package com.openclassrooms.realestatemanager.domain.interactors.searchProperty

import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import javax.inject.Inject

class SearchProperties @Inject constructor(private val propertyRepositoryImpl: PropertyRepository_Impl) {
    suspend operator fun invoke(query: String, type: List<String>?) =
        propertyRepositoryImpl.searchProperties(query)
    /* private val propertyDao: PropertyDao,
     private val propertyEntityMapper: PropertyEntityMapper
 ) {
     fun execute(query: String): Flow<DataState<List<Property>>> = flow {
         *//* try {
             emit(DataState.loading(null))

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
         }*//*
    }*/


}

