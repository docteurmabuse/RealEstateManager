package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.Event
import com.openclassrooms.realestatemanager.utils.GeocodeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addProperty: AddProperty,
    private val updateProperty: AddProperty,
    application: Application,
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

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

    var agent = MutableLiveData<Agent>()

    var address = MutableLiveData<Address>()
    var address1 = MutableLiveData<String>()
    var address2 = MutableLiveData<String>()
    var city = MutableLiveData<String>()
    var zipCode = MutableLiveData<String>()
    var state = MutableLiveData<String>()
    var area = MutableLiveData<String>()
    var country = MutableLiveData<String>()
    var lat = MutableLiveData<Double>()
    var long = MutableLiveData<Double>()

    var photos: ArrayList<Media.Photo> = arrayListOf()
    var videos: ArrayList<Media.Video> = arrayListOf()
    var geocoder: Geocoder? = null


    //Snackbar with message to user if a field is empty
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val isNewProperty: Boolean = true
    private var _statePhotos =
        MutableStateFlow<ArrayList<Media.Photo>>(arrayListOf())
    val statePhotos: StateFlow<List<Media.Photo>>
        get() = _statePhotos

    fun saveProperty() {
        val currentId = UUID.randomUUID().toString()
        val currentType = type.value
        val currentPrice = price.value
        val currentSurface = surface.value
        val currentRoomNumber = roomNumber.value
        val currentBathroomNumber = bathroomNumber.value
        val currentBedroomNumber = bedroomNumber.value
        val currentDescription = description.value
        val currentSchools = schools.value
        val currentShops = shops.value
        val currentPark = park.value
        val currentStations = stations.value
        val currentHospital = hospital.value
        val currentMuseum = museum.value
        val currentSold = sold.value
        val currentSellDate = sellDate.value
        val currentSoldDate = soldDate.value
        val media = Media(photos, videos)
        val currentAgent = agent.value
        val currentAddress1 = address1.value
        val currentAddress2 = address2.value
        val currentCity = city.value
        val currentZipcode = zipCode.value
        val currentArea = area.value
        val currentState = state.value
        val currentCountry = country.value


        val addressLine =
            "$currentAddress1, $currentCity, $currentState, $currentZipcode, $currentCountry"

        val location = GeocodeUtils.getLatLngFromAddress(addressLine, context)
        Timber.d("PROPERTY_VIEWMODEL3: $location , $context")

        val currentLat = location?.latitude
        val currentLong = location?.longitude

        val currentAddress = Address(
            currentAddress1,
            currentAddress2,
            currentCity,
            currentZipcode,
            currentState,
            currentCountry,
            currentArea,
            currentLat,
            currentLong
        )


/*
        if (currentType == null || currentPrice == null || currentSurface == null ||
            currentRoomNumber == null || currentBathroomNumber == null || currentBedroomNumber == null ||
            currentDescription == null || currentMedia == null || currentAgent == null || currentAddress == null
            || currentSellDate == null
        ) {
            _snackbarText.value = Event(R.string.empty_property_message)
            return
        }

        if (Property(
                currentId,
                currentType,
                currentPrice,
                currentSurface,
                currentRoomNumber,
                currentBathroomNumber,
                currentBedroomNumber,
                currentDescription,
                currentSchools,
                currentShops,
                currentPark,
                currentStations,
                currentHospital,
                currentMuseum,
                currentSold,
                currentSellDate,
                currentSoldDate,
                currentMedia,
                currentAgent,
                currentAddress
            ).isEmpty
        ) {
            _snackbarText.value = Event(R.string.empty_property_message)
            return
        }*/
        createProperty(
            Property(
                currentId,
                currentType,
                currentPrice,
                currentSurface,
                currentRoomNumber,
                currentBathroomNumber,
                currentBedroomNumber,
                currentDescription,
                currentSchools,
                currentShops,
                currentPark,
                currentStations,
                currentHospital,
                currentMuseum,
                currentSold,
                currentSellDate,
                currentSoldDate,
                media,
                currentAgent,
                currentAddress
            )
        )
//        if (isNewProperty || currentId == null) {
//
//        }
        var myTestProperty = Property(
            currentId,
            currentType,
            currentPrice,
            currentSurface,
            currentRoomNumber,
            currentBathroomNumber,
            currentBedroomNumber,
            currentDescription,
            currentSchools,
            currentShops,
            currentPark,
            currentStations,
            currentHospital,
            currentMuseum,
            currentSold,
            currentSellDate,
            currentSoldDate,
            media,
            currentAgent,
            currentAddress
        )
        Timber.d("PROPERTY_VIEWMODEL3: $myTestProperty")

    }

    private fun createProperty(property: Property) {
        viewModelScope.launch {
            Timber.d("PROPERTY: ${property.agent}, ${property.address}")
            addProperty.invoke(property)
        }
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

    fun updatePropertyToRoomDb(property: Property) {
        Timber.tag("UPDATE_FabClick").d("UPDATE_FabClick: $property")

        viewModelScope.launch {
            // property.agentId = currentAgentId
            // Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            updateProperty.invoke(property)
        }
    }


}
