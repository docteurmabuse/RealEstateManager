package com.openclassrooms.realestatemanager.domain.model.agent

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.openclassrooms.realestatemanager.BR
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agent(
    var _id: String? = "",
    var _name: String? = "",
    var _email: String? = "",
    var _phone: String? = "",
    var _photoUrl: String? = ""
) : Parcelable, BaseObservable() {
    @get:Bindable
    var id
        get() = _id
        set(value) {
            _id = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    var name
        get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var email
        get() = _email
        set(value) {
            _email = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var phone
        get() = _phone
        set(value) {
            _phone = value
            notifyPropertyChanged(BR.phone)
        }

    @get:Bindable
    var photoUrl
        get() = _photoUrl
        set(value) {
            _photoUrl = value
            notifyPropertyChanged(BR.photoUrl)
        }

    val isEmpty
        get() = name!!.isEmpty() || email!!.isEmpty() || phone!!.isEmpty()
}
