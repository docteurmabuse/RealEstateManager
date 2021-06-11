package com.openclassrooms.realestatemanager.domain.model.search

data class SearchFilters(
    val textQuery: String = "",
    val museum: Int? = -1,
    val school: Int? = -1,
    val shop: Int? = -1,
    val hospital: Int? = -1,
    val station: Int? = -1,
    val park: Int? = -1,
    val area: String?,
    val types: List<String?>?,
)
