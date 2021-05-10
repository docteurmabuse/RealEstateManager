package com.openclassrooms.realestatemanager.domain.model.property


data class Video(
    var id: Int,
    var propertyId: Int,
    var name: String?,
    var videoPath: String
)
