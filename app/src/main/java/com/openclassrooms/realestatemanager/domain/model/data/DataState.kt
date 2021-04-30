package com.openclassrooms.realestatemanager.domain.model.data

class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false
)