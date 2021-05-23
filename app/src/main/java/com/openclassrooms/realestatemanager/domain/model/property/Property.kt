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
    var type: String? = "",
    @get:Bindable
    var price: Int? = null,
    @get:Bindable
    var surface: Int? = null,
    @get:Bindable
    var roomNumber: Int? = null,
    @get:Bindable
    var bathroomNumber: Int? = null,
    @get:Bindable
    var bedroomNumber: Int? = null,
    @get:Bindable
    var description: String? = "",
    @get:Bindable
    var schools: Boolean = false,
    @get:Bindable
    var shops: Boolean = false,
    @get:Bindable
    var park: Boolean = false,
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
    var agentId: String = "",
    @get:Bindable
    var address: Address? = null
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
