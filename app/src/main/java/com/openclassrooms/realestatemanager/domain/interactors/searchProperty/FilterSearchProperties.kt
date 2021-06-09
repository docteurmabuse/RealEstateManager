package com.openclassrooms.realestatemanager.domain.interactors.searchProperty

import com.openclassrooms.realestatemanager.domain.model.search.SearchFilters
import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import javax.inject.Inject

class FilterSearchProperties @Inject constructor(private val propertyRepositoryImpl: PropertyRepository_Impl) {
    suspend operator fun invoke(
        query: SearchFilters
    ) =
        propertyRepositoryImpl.filterSearchProperties(
            query
        )
}
