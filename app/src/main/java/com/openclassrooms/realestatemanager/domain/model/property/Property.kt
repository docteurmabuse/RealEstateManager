package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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
    var schools: Boolean = false,
    var shops: Boolean = false,
    var parcs: Boolean = false,
    var stations: Boolean = false,
    var hospital: Boolean = false,
    var museum: Boolean = false,
    var sold: Boolean = false,
    var sellDate: Date?,
    var soldDate: Date?,
    var media: Media,
    var agentId: String = "1"
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
