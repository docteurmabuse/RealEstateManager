package com.openclassrooms.realestatemanager.domain.model

data class Photo(
    var id: Int,
    var propertyId: Int,
    var name: String?,
    var room: String,
    var photoPath: String
)
