package com.openclassrooms.realestatemanager.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Property(
    var id: Int,
    var type: String = "House",
    var price: Int?,
    var surface: Int?,
    var roomNumber: Int?,
    var bathroomNumber: Int?,
    var bedroomNumber: Int?,
    var description: String?,
    var address1: String,
    var address2: String?,
    var city: String = "New York",
    var zipCode: Int,
    var state: String? = "NY",
    var country: String = "United States",
    var area: String?,
    var schools: Boolean?,
    var shops: Boolean?,
    var parcs: Boolean?,
    var stations: Boolean?,
    var hospital: Boolean?,
    var museum: Boolean?,
    var sold: Boolean = false,
    var sellDate: Date,
    var soldDate: Date,
    var agentId: Int = 1
) : Parcelable
