package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

@Parcelize
data class Property(
    var id: Long,
    var type: PropertyType?,
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
    var media: @RawValue Media,
    var agentId: Long = 1
) : Parcelable {

    enum class PropertyType {
        House,
        Flat,
        Duplex,
        Penthouse,
        Manor,
        Loft
    }


}
