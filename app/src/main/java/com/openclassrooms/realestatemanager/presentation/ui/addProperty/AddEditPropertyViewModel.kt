package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.presentation.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addProperty: AddProperty,
    private val updateProperty: AddProperty
) : ViewModel() {

    // Two-way databinding, exposing MutableLiveData
    var id = MutableLiveData<String>()
    var type = MutableLiveData<String>()
    var price = MutableLiveData<Int>()
    var surface = MutableLiveData<Int>()
    var roomNumber = MutableLiveData<Int>()
    var bathroomNumber = MutableLiveData<Int>()
    var bedroomNumber = MutableLiveData<Int>()
    var description = MutableLiveData<String>()
    var schools = MutableLiveData<Boolean>()
    var shops = MutableLiveData<Boolean>()
    var park = MutableLiveData<Boolean>()
    var stations = MutableLiveData<Boolean>()
    var hospital = MutableLiveData<Boolean>()
    var museum = MutableLiveData<Boolean>()
    var sold = MutableLiveData<Boolean>()
    var sellDate = MutableLiveData<Date>()
    var soldDate = MutableLiveData<Date>()
    var media = MutableLiveData<Media>()
    var agent = MutableLiveData<Agent>()
    var address = MutableLiveData<Address>()

    //Snackbar with message to user if a field is empty
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun saveProperty() {
        val currentId = id.value
        val currentType = type.value
        val currentPrice = price.value
        val currentSurface = surface.value
        val currentRoomNumber = roomNumber.value
        val currentBathroomNumber = bathroomNumber.value
        val currentBedroomNumber = bedroomNumber.value
        val currentDescrition = description.value
        val currentSchools = schools.value
        val currentShops = shops.value
        val currentPark = park.value
        val currentStations = stations.value
        val currentHopsital = hospital.value
        val currentMuseum = museum.value
        val currentSold = sold.value
        val currentSellDate = sellDate.value
        val currentSoldDate = soldDate.value
        val currentMedia = media.value
        val currentAgent = agent.value
        val currentAddress = address.value
    }
}
