package com.openclassrooms.realestatemanager.domain.model.property

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @get:Bindable
    var address1: String? = "",
    @get:Bindable
    var address2: String? = "",
    @get:Bindable
    var city: String? = "New York",
    @get:Bindable
    var zipCode: String? = "",
    @get:Bindable
    var state: String? = "NY",
    @get:Bindable
    var country: String? = "United States",
    @get:Bindable
    var area: String?,
    @get:Bindable
    var lat: Double?,
    @get:Bindable
    var lng: Double?
) : Parcelable, BaseObservable()
