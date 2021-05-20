package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addProperty: AddProperty
) : ViewModel() {
    fun addPropertyToRoomDb(property: String) {
        Timber.tag("FabClick").d("It's ok FAB2: $property")

        viewModelScope.launch {
            val currentAgentId = FirebaseAuth.getInstance().currentUser.uid
            // property.agentId = currentAgentId
            // Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            // addProperty.invoke(property)
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
        if (addPropertyView.price != null) {
            property.price = addPropertyView.price!!.toInt()
        } else {
            property.price = 0
        }
        if (addPropertyView.surface != null) {
            property.surface = addPropertyView.surface!!.toInt()
        } else {
            property.surface = 0
        }
        if (addPropertyView.roomNumber != null) {
            property.roomNumber = addPropertyView.roomNumber!!.toInt()
        } else {
            property.bathroomNumber = 0
        }
        if (addPropertyView.bathroomNumber != null) {
            property.bathroomNumber = addPropertyView.bathroomNumber!!.toInt()
        } else {
            property.bathroomNumber = 0
        }
        if (addPropertyView.bedroomNumber != null) {
            property.bedroomNumber = addPropertyView.bedroomNumber!!.toInt()
        } else {
            property.bedroomNumber = 0
        }
        property.description = addPropertyView.description
        property.address1 = addPropertyView.address1
        property.address2 = addPropertyView.address2
        property.city = addPropertyView.city
        if (addPropertyView.zipcode != null) {
            property.zipCode = addPropertyView.zipcode!!.toInt()
        } else {
            property.bedroomNumber = 0
        }
        property.state = addPropertyView.state
        property.country = addPropertyView.country
        property.area = addPropertyView.area
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
        var type: String? = "null",
        var price: String? = "",
        var surface: String? = "",
        var roomNumber: String? = "",
        var bathroomNumber: String? = "",
        var bedroomNumber: String? = "",
        var description: String? = "",
        var address1: String = "",
        var address2: String? = "",
        var city: String = "New York",
        var zipcode: String? = null,
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

