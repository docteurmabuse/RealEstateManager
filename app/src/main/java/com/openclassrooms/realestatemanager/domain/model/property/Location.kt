package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    @get:Bindable
    var location: Location? = null
) : Parcelable
