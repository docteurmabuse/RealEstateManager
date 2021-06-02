package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.R
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
    var test: String = ""
    var propertyType = Property.PropertyType.values()

    // Two-way databinding, exposing MutableLiveData
    var propertyId = MutableLiveData<String>()
    var type = MutableLiveData<String>("")
    var price = MutableLiveData<Int>(0)
    var surface = MutableLiveData<Int>(0)
    var roomNumber = MutableLiveData<Int>(0)
    var bathroomNumber = MutableLiveData<Int>(0)
    var bedroomNumber = MutableLiveData<Int>(0)
    var description = MutableLiveData<String>()
    var schools = MutableLiveData<Boolean>(false)
    var shops = MutableLiveData<Boolean>(false)
    var park = MutableLiveData<Boolean>(false)
    var stations = MutableLiveData<Boolean>(false)
    var hospital = MutableLiveData<Boolean>(false)
    var museum = MutableLiveData<Boolean>(false)
    var sold = MutableLiveData<Boolean>(false)
    var sellDate = MutableLiveData<Date>()
    var soldDate = MutableLiveData<Date>()
    var photos = MutableLiveData<List<Media.Photo>>(arrayListOf())
    var videos = MutableLiveData<List<Media.Video>>(arrayListOf())
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


    //Snackbar with message to user if a field is empty
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val isNewProperty: Boolean = true
    private var _statePhotos =
        MutableStateFlow<ArrayList<Media.Photo>>(arrayListOf())
    val statePhotos: StateFlow<List<Media.Photo>>
        get() = _statePhotos

    fun saveProperty() {
        val currentId = propertyId.value
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
        var currentPhotos = photos.value
        val currentVideos = videos.value
        val currentAgentId = agent.value?.id
        val currentAddress1 = address1.value
        val currentAddress2 = address2.value
        val currentCity = city.value
        val currentZipcode = zipCode.value
        val currentArea = area.value
        val currentState = state.value
        val currentCountry = country.value


        viewModelScope.launch {
            currentPhotos = statePhotos.value
            Timber.tag("STATE_PHOTO").d("STATE_PHOTO: ${_statePhotos.value}")
        }

        val currentMedia = Media(currentPhotos!!, currentVideos!!)


        val addressLine =
            "$currentAddress1, $currentCity, $currentState, $currentZipcode, $currentCountry"

        val location = GeocodeUtils.getLatLngFromAddress(addressLine, context)

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
        val myTestProperty = Property(
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
            currentAgentId,
            currentAddress
        )
        Timber.d("PROPERTY_VIEWMODEL_TEST: $myTestProperty")
        Timber.d("PROPERTY_VIEWMODEL3: $location , $context")

        if (currentType == null || currentPrice == null || currentSurface == null ||
            currentRoomNumber == null || currentBathroomNumber == null || currentBedroomNumber == null ||
            currentDescription == null || currentMedia == null || currentAgentId == null || currentAddress == null
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
                currentAgentId,
                currentAddress
            ).isEmpty
        ) {
            _snackbarText.value = Event(R.string.empty_property_message)
            return
        }


        if (isNewProperty || currentId == null) {
            createProperty(
                Property(
                    UUID.randomUUID().toString(),
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
                    currentAgentId,
                    currentAddress
                )
            )
        }


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
