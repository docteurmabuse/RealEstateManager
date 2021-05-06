package com.openclassrooms.realestatemanager.domain.model

data class Photo(
    var id: Long,
    var propertyId: Int,
    var name: String?,
    var photoPath: String
)
