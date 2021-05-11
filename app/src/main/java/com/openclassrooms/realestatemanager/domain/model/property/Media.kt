package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Media(
    val photos: @RawValue List<Photo>,
    val videos: @RawValue List<Video>
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

