package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val photos: List<Photo> = arrayListOf(),
    val videos: List<Video> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class Photo(
        var name: String?,
        var photoPath: String
    ) : Parcelable

    @Parcelize
    data class Video(
        var name: String?,
        var videoPath: String
    ) : Parcelable
}

