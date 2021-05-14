package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Property(
    @get:Bindable
    var id: Long = 0,
    @get:Bindable
    var type: PropertyType? = null,
    @get:Bindable
    var price: Int? = 0,
    @get:Bindable
    var surface: Int? = 0,
    @get:Bindable
    var roomNumber: Int? = 1,
    @get:Bindable
    var bathroomNumber: Int? = 1,
    @get:Bindable
    var bedroomNumber: Int? = 1,
    @get:Bindable
    var description: String? = "",
    @get:Bindable
    var address1: String = "",
    @get:Bindable
    var address2: String? = "",
    @get:Bindable
    var city: String = "New York",
    @get:Bindable
    var zipCode: Int? = null,
    @get:Bindable
    var state: String? = "NY",
    @get:Bindable
    var country: String = "United States",
    @get:Bindable
    var area: String? = "",
    @get:Bindable
    var schools: Boolean = false,
    @get:Bindable
    var shops: Boolean = false,
    @get:Bindable
    var parcs: Boolean = false,
    @get:Bindable
    var stations: Boolean = false,
    @get:Bindable
    var hospital: Boolean = false,
    @get:Bindable
    var museum: Boolean = false,
    @get:Bindable
    var sold: Boolean = false,
    @get:Bindable
    var sellDate: Date? = null,
    @get:Bindable
    var soldDate: Date? = null,
    @get:Bindable
    var media: Media = Media(arrayListOf(), arrayListOf()),
    @get:Bindable
    var agentId: String = "1"
) : Parcelable, BaseObservable() {

    enum class PropertyType {
        House,
        Flat,
        Duplex,
        Penthouse,
        Manor,
        Loft
    }


}