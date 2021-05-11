package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val photos: List<Photo>,
    val videos: List<Video>
) : Parcelable {
    data class Photo(
        var name: String?,
        var photoPath: String
    )

    data class Video(
        var name: String?,
        var videoPath: String
    )
}

