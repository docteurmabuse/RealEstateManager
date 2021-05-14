package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.content.Context
import android.graphics.Bitmap
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

    fun setImage(context: Context, image: Bitmap) {

    }


    data class AddPropertyView(
        var id: Long? = null,
        var type: Property.PropertyType? = null,
        var price: Int? = 0,
        var surface: Int? = 0,
        var roomNumber: Int? = 1,
        var bathroomNumber: Int? = 1,
        var bedroomNumber: Int? = 1,
        var description: String? = "",
        var address1: String = "",
        var address2: String? = "",
        var city: String = "New York",
        var zipCode: Int? = null,
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

