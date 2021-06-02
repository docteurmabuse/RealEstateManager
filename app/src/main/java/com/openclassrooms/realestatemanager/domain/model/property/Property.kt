package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.openclassrooms.realestatemanager.BR
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Property(
    var _id: String? = "",
    var _type: String? = "",
    var _price: Int? = null,
    var _surface: Int? = null,
    var _roomNumber: Int? = null,
    var _bathroomNumber: Int? = null,
    var _bedroomNumber: Int? = null,
    var _description: String? = "",
    var _schools: Boolean? = false,
    var _shops: Boolean? = false,
    var _park: Boolean? = false,
    var _stations: Boolean? = false,
    var _hospital: Boolean? = false,
    var _museum: Boolean? = false,
    var _sold: Boolean? = false,
    var _sellDate: Date? = null,
    var _soldDate: Date? = null,
    var _media: Media = Media(arrayListOf(), arrayListOf()),
    var _agent: String? = null,
    var _address: Address? = null
) : Parcelable, BaseObservable() {
    @get:Bindable
    var id
        get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var type
        get() = _type
        set(value) {
            _type = value
            notifyPropertyChanged(BR.type)
        }

    @get:Bindable
    var price
        get() = _price
        set(value) {
            _price = value
            notifyPropertyChanged(BR.price)
        }

    @get:Bindable
    var surface
        get() = _surface
        set(value) {
            _surface = value
            notifyPropertyChanged(BR.surface)
        }

    @get:Bindable
    var roomNumber
        get() = _roomNumber
        set(value) {
            _roomNumber = value
            notifyPropertyChanged(BR.roomNumber)
        }

    @get:Bindable
    var bathroomNumber
        get() = _bathroomNumber
        set(value) {
            _bathroomNumber = value
            notifyPropertyChanged(BR.bathroomNumber)
        }

    @get:Bindable
    var bedroomNumber
        get() = _bedroomNumber
        set(value) {
            _bedroomNumber = value
            notifyPropertyChanged(BR.bedroomNumber)
        }

    @get:Bindable
    var description
        get() = _description
        set(value) {
            _description = value
            notifyPropertyChanged(BR.description)
        }

    @get:Bindable
    var schools
        get() = _schools
        set(value) {
            _schools = value
            notifyPropertyChanged(BR.schools)
        }

    @get:Bindable
    var shops
        get() = _shops
        set(value) {
            _shops = value
            notifyPropertyChanged(BR.shops)
        }

    @get:Bindable
    var park
        get() = _park
        set(value) {
            _park = value
            notifyPropertyChanged(BR.park)
        }

    @get:Bindable
    var stations
        get() = _stations
        set(value) {
            _stations = value
            notifyPropertyChanged(BR.stations)
        }

    @get:Bindable
    var hospital
        get() = _hospital
        set(value) {
            _hospital = value
            notifyPropertyChanged(BR.hospital)
        }

    @get:Bindable
    var museum
        get() = _museum
        set(value) {
            _museum = value
            notifyPropertyChanged(BR.museum)
        }

    @get:Bindable
    var sold
        get() = _sold
        set(value) {
            _sold = value
            notifyPropertyChanged(BR.sold)
        }

    @get:Bindable
    var sellDate
        get() = _sellDate
        set(value) {
            _sellDate = value
            notifyPropertyChanged(BR.sellDate)
        }

    @get:Bindable
    var soldDate
        get() = _soldDate
        set(value) {
            _soldDate = value
            notifyPropertyChanged(BR.soldDate)
        }

    @get:Bindable
    var media
        get() = _media
        set(value) {
            _media = value
            notifyPropertyChanged(BR.media)
        }

    @get:Bindable
    var agent
        get() = _agent
        set(value) {
            _agent = value
            notifyPropertyChanged(BR.agent)
        }

    @get:Bindable
    var address
        get() = _address
        set(value) {
            _address = value
            notifyPropertyChanged(BR.address)
        }

    val isEmpty
        get() = type!!.isEmpty() || price != 0 || surface != 0 || roomNumber != 0 || bathroomNumber != 0 || bedroomNumber != 0 || description!!.isEmpty()
                || media.photos.isEmpty() || agent!!.isNotBlank() || address != null || sellDate != null

    enum class PropertyType {
        House,
        Flat,
        Duplex,
        Penthouse,
        Manor,
        Loft
    }
}
