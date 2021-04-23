package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
        val id: Int
) : Parcelable
