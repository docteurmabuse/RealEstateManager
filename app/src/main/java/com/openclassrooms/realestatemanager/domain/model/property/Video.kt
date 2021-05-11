package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    var id: Int,
    var propertyId: Int,
    var name: String?,
    var videoPath: String
) : Parcelable
