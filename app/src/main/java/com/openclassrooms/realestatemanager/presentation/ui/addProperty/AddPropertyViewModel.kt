package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.os.Bundle
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
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
    private val addProperty: AddProperty
) : ViewModel() {
    private val _statePhotos =
        MutableStateFlow<ArrayList<Media.Photo>>(arrayListOf())
    val statePhotos: StateFlow<List<Media.Photo>>
        get() = _statePhotos

    fun addPropertyToRoomDb(property: String) {
        Timber.tag("FabClick").d("It's ok FAB2: $property")

        viewModelScope.launch {
            val currentAgentId = FirebaseAuth.getInstance().currentUser.uid
            // property.agentId = currentAgentId
            // Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            // addProperty.invoke(property)
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
        property.address?.address1 = addPropertyView.address1
        property.address?.address2 = addPropertyView.address2
        property.address?.city = addPropertyView.city
        if (addPropertyView.zipcode!!.isNotEmpty()) {
            property.address?.zipCode = addPropertyView.zipcode!!.toInt()
        } else {
            property.address?.zipCode = 10000
        }
        property.address?.state = addPropertyView.state
        property.address?.country = addPropertyView.country
        property.address?.area = addPropertyView.area
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
        property.agentId = addPropertyView.agentId
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
        var address1: String? = "",
        var address2: String? = "",
        var city: String = "New York",
        var zipcode: String? = "",
        var state: String? = "NY",
        var country: String = "United States",
        var area: String? = "",
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
        var agentId: String = "1"
    )
}

