package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val photos: List<Photo> = arrayListOf(),
    val videos: List<Video> = arrayListOf()
) : Parcelable, BaseObservable() {
    @Parcelize
    data class Photo(
        @get:Bindable
        var name: String?,
        @get:Bindable
        var photoPath: String
    ) : Parcelable, BaseObservable()

    @Parcelize
    data class Video(
        @get:Bindable
        var name: String?,
        @get:Bindable
        var videoPath: String
    ) : Parcelable, BaseObservable()
}

