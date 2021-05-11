package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    var id: Long,
    var propertyId: Int,
    var name: String?,
    var photoPath: String
) : Parcelable
