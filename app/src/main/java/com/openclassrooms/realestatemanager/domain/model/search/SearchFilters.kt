package com.openclassrooms.realestatemanager.domain.model.search

import javax.annotation.Nullable

data class SearchFilters(
    @Nullable
    val textQuery: String = "",
    @Nullable
    val museum: Int? = -1,
    @Nullable
    val school: Int? = -1,
    @Nullable
    val shop: Int? = -1,
    @Nullable
    val hospital: Int? = -1,
    @Nullable
    val station: Int? = -1,
    @Nullable
    val park: Int? = -1,
    @Nullable
    val area: String?,
    @Nullable
    val types: List<String>?,
)
