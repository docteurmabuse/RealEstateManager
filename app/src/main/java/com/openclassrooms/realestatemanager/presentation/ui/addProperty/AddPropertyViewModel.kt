package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.os.Bundle
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.ui.property.PropertyDetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addProperty: AddProperty,
    private val updateProperty: AddProperty

) : ViewModel() {
    private var _statePhotos =
        MutableStateFlow<ArrayList<Media.Photo>>(arrayListOf())
    val statePhotos: StateFlow<List<Media.Photo>>
        get() = _statePhotos

    private val _state =
        MutableStateFlow<DataState<ArrayList<Media.Photo>>>(DataState.loading(null))
    val state: StateFlow<DataState<ArrayList<Media.Photo>>>
        get() = _state


    fun addPropertyToRoomDb(property: Property) {
        Timber.tag("FabClick").d("It's ok FAB2: $property")

        viewModelScope.launch {
            // property.agentId = currentAgentId
            // Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            addProperty.invoke(property)
        }
    }

    fun updatePropertyToRoomDb(property: Property) {
        Timber.tag("UPDATE_FabClick").d("UPDATE_FabClick: $property")

        viewModelScope.launch {
            // property.agentId = currentAgentId
            // Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            updateProperty.invoke(property)
        }
    }

    val onClickListener = View.OnClickListener { itemView ->
        val item = itemView.tag as Property
        val bundle = Bundle()
        bundle.putParcelable(
            PropertyDetailFragment.ARG_PROPERTY,
            item
        )
    }

    fun initPhotosList(photos: ArrayList<Media.Photo>) {
        viewModelScope.launch {
            _statePhotos.value.addAll(photos)
            Timber.tag("STATE_PHOTO").d("STATE_PHOTO: ${_statePhotos.value}")
        }
    }

    fun addPhotoToPhotosList(photo: Media.Photo) {
        viewModelScope.launch {
            _statePhotos.value.add(photo)
            Timber.tag("STATE_PHOTO").d("STATE_PHOTO: ${_statePhotos.value}")
        }
    }

    fun removePhotoToPhotosList(photo: Media.Photo) {
        viewModelScope.launch {
            _statePhotos.value.remove(photo)
            Timber.tag("STATE_PHOTO").d("STATE_PHOTO: ${_statePhotos.value}")
        }
    }


    fun saveProperty(addPropertyView: AddPropertyView) {
        viewModelScope.launch {
            val property: Property = addPropertyViewToProperty(addPropertyView)
            Timber.tag("SAVE_PROPERTY").d("SAVE_PROPERTY: $property")

            property.let { addProperty.invoke(it) }
        }
    }

    private fun addPropertyViewToProperty(addPropertyView: AddPropertyView): Property {
        val property = Property()
        property.id = addPropertyView.id
        property.type = addPropertyView.type
        if (addPropertyView.price!!.isNotEmpty()) {
            property.price = addPropertyView.price!!.toInt()
        } else {
            property.price = 0
        }
        if (addPropertyView.surface!!.isNotEmpty()) {
            property.surface = addPropertyView.surface!!.toInt()
        } else {
            property.surface = 0
        }
        if (addPropertyView.roomNumber!!.isNotEmpty()) {
            property.roomNumber = addPropertyView.roomNumber!!.toInt()
        } else {
            property.bathroomNumber = 0
        }
        if (addPropertyView.bathroomNumber!!.isNotEmpty()) {
            property.bathroomNumber = addPropertyView.bathroomNumber!!.toInt()
        } else {
            property.bathroomNumber = 0
        }
        if (addPropertyView.bedroomNumber!!.isNotEmpty()) {
            property.bedroomNumber = addPropertyView.bedroomNumber!!.toInt()
        } else {
            property.bedroomNumber = 0
        }
        property.description = addPropertyView.description
        property.schools = addPropertyView.schools
        property.shops = addPropertyView.shops
        property.park = addPropertyView.parcs
        property.stations = addPropertyView.stations
        property.hospital = addPropertyView.hospital
        property.museum = addPropertyView.museum
        property.sold = addPropertyView.sold
        property.sellDate = addPropertyView.sellDate
        property.soldDate = addPropertyView.soldDate
        property.media = addPropertyView.media
        property.agent = addPropertyView.agent
        property.address = addPropertyView.address
        return property
    }

    data class AddPropertyView(
        var id: Long = 0,
        var type: String? = "",
        var price: String? = "",
        var surface: String? = "",
        var roomNumber: String? = "",
        var bathroomNumber: String? = "",
        var bedroomNumber: String? = "",
        var description: String? = "",
        var schools: Boolean = false,
        var shops: Boolean = false,
        var parcs: Boolean = false,
        var stations: Boolean = false,
        var hospital: Boolean = false,
        var museum: Boolean = false,
        var sold: Boolean = false,
        var sellDate: Date? = null,
        var soldDate: Date? = null,
        var media: Media = Media(arrayListOf(), arrayListOf()),
        var agent: Agent? = null,
        var address: Address?

    )
}

