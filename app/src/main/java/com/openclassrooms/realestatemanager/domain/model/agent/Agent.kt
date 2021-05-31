package com.openclassrooms.realestatemanager.domain.model.agent

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agent(
    @get:Bindable
    var id: String,
    @get:Bindable
    var name: String?,
    @get:Bindable
    var email: String?,
    @get:Bindable
    var phone: String?,
    @get:Bindable
    var photoUrl: String?
) : Parcelable, BaseObservable()
