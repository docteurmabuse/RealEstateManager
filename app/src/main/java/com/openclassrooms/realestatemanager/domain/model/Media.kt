package com.openclassrooms.realestatemanager.domain.model

data class Media(
    val photos: List<Photo>,
    val videos: List<Video>
) {
    data class Photo(
        var name: String?,
        var photoPath: String
    )

    data class Video(
        var name: String?,
        var videoPath: String
    )
}

