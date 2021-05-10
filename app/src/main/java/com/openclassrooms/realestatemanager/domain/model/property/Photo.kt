package com.openclassrooms.realestatemanager.domain.model.property

data class Photo(
    var id: Long,
    var propertyId: Int,
    var name: String?,
    var photoPath: String
)
