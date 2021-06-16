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
    val minSurface: Float?,
    val maxSurface: Float?,
    val minPrice: Float?,
    val maxPrice: Float?,
    val sold: Int? = -1,
    val sellDate: Long? = -1,
    val soldDate: Long? = -1,
    val numberOfPics: Float?,
    val rooms: Int,
    val beds: Int,
    val baths: Int
)
