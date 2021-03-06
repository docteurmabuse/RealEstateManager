package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.interactors.agent.GetAgentById
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.interactors.property.GetPropertyById
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.Event
import com.openclassrooms.realestatemanager.utils.DateUtil
import com.openclassrooms.realestatemanager.utils.GeocodeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditPropertyViewModel @Inject constructor(
    private val addProperty: AddProperty,
    private val getPropertyById: GetPropertyById,
    private val getAgentById: GetAgentById,
    application: Application,
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    // For Type dropdown
    val propertyType = Property.PropertyType.values()

    // Two-way databinding, exposing MutableLiveData
    private var propertyId: String? = null
    val type = MutableLiveData<String>("House")
    val price = MutableLiveData<String>("")
    val surface = MutableLiveData<String>("")
    val roomNumber = MutableLiveData<String>("")
    val bathroomNumber = MutableLiveData<String>("")
    val bedroomNumber = MutableLiveData<String>("")
    val description = MutableLiveData<String>("")
    val schools = MutableLiveData<Boolean>(false)
    val shops = MutableLiveData<Boolean>(false)
    val park = MutableLiveData<Boolean>(false)
    val stations = MutableLiveData<Boolean>(false)
    val hospital = MutableLiveData<Boolean>(false)
    val museum = MutableLiveData<Boolean>(false)
    var sold = MutableLiveData<Boolean>(false)
    var sellDate = MutableLiveData<String>(DateUtil.todayDate)
    var soldDate = MutableLiveData<String>(DateUtil.todayDate)
    val photos = MutableLiveData<List<Media.Photo>>(arrayListOf())
    val videos = MutableLiveData<List<Media.Video>>(arrayListOf())
    val agent = MutableLiveData<Agent>()
    val agentId = MutableLiveData<String>("")
    val address = MutableLiveData<Address>()
    val address1 = MutableLiveData<String?>("")
    val address2 = MutableLiveData<String?>("")
    val city = MutableLiveData<String?>("New York")
    val zipCode = MutableLiveData<String?>("")
    val state = MutableLiveData<String?>("NY")
    val area = MutableLiveData<String?>("")
    val country = MutableLiveData<String?>("United States")
    val lat = MutableLiveData<Double>(0.0)
    val long = MutableLiveData<Double>(0.0)
    val isNewProperty = MutableLiveData<Boolean>(false)

    //Snackbar with message to user if a field is empty
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    private var _statePhotos =
        MutableStateFlow<ArrayList<Media.Photo>>(arrayListOf())
    val statePhotos: StateFlow<List<Media.Photo>>
        get() = _statePhotos
    private val _state = MutableStateFlow<DataState<Property>>(DataState.loading(null))
    val propertyState: StateFlow<DataState<Property>>
        get() = _state
    private val _stateAgent = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val agentState: StateFlow<DataState<Agent>>
        get() = _stateAgent
    private val _propertyAddedEvent = MutableLiveData<Event<Unit>>()
    val propertyAddedEvent: LiveData<Event<Unit>> = _propertyAddedEvent
    private val _propertyUpdatedEvent = MutableLiveData<Event<Unit>>()
    val propertyUpdatedEvent: LiveData<Event<Unit>> = _propertyUpdatedEvent
    fun start(propertyId: String) {
        this.propertyId = propertyId
        if (propertyId.isBlank()) {
            isNewProperty.value = true
            Timber.d("EDIT_MODE: TRUE")
            return
        } else {
            isNewProperty.value = false
            Timber.d("EDIT_MODE: FALSE")
            viewModelScope.launch {
                getPropertyById.invoke(propertyId)
                    .catch { e ->
                        _state.value = (DataState.error(e.toString(), null))
                    }
                    .collectLatest {
                        _state.value = DataState.success(it)
                        onPropertyLoaded(it)
                    }
            }
        }
    }

    private fun onPropertyLoaded(property: Property) {
        // Two-way databinding, exposing MutableLiveData
        propertyId = property.id
        type.value = property.type
        price.value = property.price.toString()
        surface.value = property.surface.toString()
        roomNumber.value = property.roomNumber.toString()
        bathroomNumber.value = property.bathroomNumber.toString()
        bedroomNumber.value = property.bedroomNumber.toString()
        description.value = property.description
        schools.value = property.schools
        shops.value = property.shops
        park.value = property.park
        stations.value = property.stations
        hospital.value = property.hospital
        museum.value = property.museum
        sold.value = property.sold
        sellDate.value = property.sellDate.toString()
        soldDate.value = property.soldDate.toString()
        videos.value = property.media.videos
        agentId.value = property.agent
        address.value = property.address
        address1.value = property.address?.address1
        address2.value = property.address?.address2
        city.value = property.address?.city
        zipCode.value = property.address?.zipCode
        state.value = property.address?.state
        area.value = property.address?.area
        country.value = property.address?.country
        lat.value = property.address?.lat.toString().toDoubleOrNull()
        long.value = property.address?.lng.toString().toDoubleOrNull()
        _statePhotos.value = property.media.photos as ArrayList<Media.Photo>
        photos.value = _statePhotos.value
        viewModelScope.launch {
            agentId.value?.let { it ->
                getAgentById.invoke(it).catch { e ->
                    _stateAgent.value = (DataState.error(e.toString(), null))
                }
                    .collectLatest {
                        _stateAgent.value = DataState.success(it)
                        onAgentLoaded(it)
                    }
            }
        }
    }

    private fun onAgentLoaded(it: Agent) {
        agent.value = it
        Timber.d("PROPERTY_AGENT: ${agent.value}")
    }

    fun saveProperty() {
        val currentId = propertyId
        val currentType = type.value
        val currentPrice = price.value?.toIntOrNull()
        val currentSurface = surface.value?.toIntOrNull()
        val currentRoomNumber = roomNumber.value?.toIntOrNull()
        val currentBathroomNumber = bathroomNumber.value?.toIntOrNull()
        val currentBedroomNumber = bedroomNumber.value?.toIntOrNull()
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
        var currentPhotos = statePhotos.value
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
        val currentMedia = Media(statePhotos.value, currentVideos!!)
        val addressLine =
            "$currentAddress1, $currentCity, $currentState, $currentZipcode, $currentCountry"
        val location = GeocodeUtils.getLatLngFromAddress(addressLine, context)
        val currentLat = location?.latitude.toString().toDoubleOrNull()
        val currentLong = location?.longitude.toString().toDoubleOrNull()
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
        if (currentType == null || currentPrice == null || currentSurface == null ||
            currentRoomNumber == null || currentBathroomNumber == null || currentBedroomNumber == null ||
            currentDescription == null || currentAgentId == null || currentAddress1 == null
            || currentCity == null || currentState == null || currentZipcode == null || currentCountry == null
            || currentSellDate == null
        ) {
            _snackbarText.value = Event(R.string.empty_property_message2)
            return
        }
        if (isNewProperty.value == true && currentId == null) {
            saveProperty(
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
                    currentSellDate.toLong(),
                    currentSoldDate?.toLong(),
                    currentMedia,
                    currentAgentId,
                    currentAddress
                )
            )
        } else {
            updatePropertyToRoomDb(
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
                    currentSellDate.toLong(),
                    currentSoldDate?.toLong(),
                    currentMedia,
                    currentAgentId,
                    currentAddress
                )
            )
            Timber.d("UPDATE: $currentSellDate")
        }
    }

    fun updatePropertyToRoomDb(property: Property) {
        if (isNewProperty.value == true) {
            throw RuntimeException("updateProperty() was called but property is new.")
        }
        viewModelScope.launch {
            addProperty.invoke(property)
            _propertyUpdatedEvent.value = Event(Unit)
        }
    }

    private fun saveProperty(property: Property) {
        viewModelScope.launch {
            Timber.d("PROPERTY: ${property.agent}, ${property.media}")
            addProperty.invoke(property)
            _propertyAddedEvent.value = Event(Unit)

        }
    }

    fun addPhotoToPhotosList(photo: Media.Photo) {
        viewModelScope.launch {
            _statePhotos.value.add(photo)
        }
    }

    fun removePhotoToPhotosList(photo: Media.Photo) {
        viewModelScope.launch {
            _statePhotos.value.remove(photo)
            photos.value?.toMutableList()?.remove(photo)
            Timber.tag("STATE_PHOTO").d("REMOVE_PHOTO: ${photos.value?.size}")
        }
    }
}
