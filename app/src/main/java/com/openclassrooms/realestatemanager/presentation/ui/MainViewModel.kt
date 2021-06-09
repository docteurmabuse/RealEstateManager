package com.openclassrooms.realestatemanager.presentation.ui

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.interactors.property.GetProperties
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.FilterSearchProperties
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.SearchProperties
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.domain.model.search.SearchFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getProperties: GetProperties,
    private val searchProperties: SearchProperties,
    private val filterSearchProperties: FilterSearchProperties,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<List<Property>>>(DataState.loading(null))
    val state: StateFlow<DataState<List<Property>>>
        get() = _state

    private val _stateFilter = MutableStateFlow<DataState<List<Property>>>(DataState.loading(null))
    val stateFilter: StateFlow<DataState<List<Property>>>
        get() = _stateFilter


    val searchQuery = MutableStateFlow("")
    var searchFilterQuery = MutableStateFlow(
        SearchFilters(
            "",
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            "",
            arrayListOf(),
        )
    )

    private val Boolean.int
        get() = if (this) 1 else -1
    var typeList = MutableLiveData<List<String>>(arrayListOf())
    var house = MutableLiveData<Boolean>(false)
    var flat = MutableLiveData<Boolean>(false)
    var duplex = MutableLiveData<Boolean>(false)
    var penthouse = MutableLiveData<Boolean>(false)
    var manor = MutableLiveData<Boolean>(false)

    var maxPrice = MutableLiveData<Float>(100000000F)
    var minPrice = MutableLiveData<Float>(0F)
    var priceArray = MutableLiveData<Array<Float>>(arrayOf())

    var minSurface = MutableLiveData<Float>(0F)
    var maxSurface = MutableLiveData<Float>(1000F)
    var surfaceArray = MutableLiveData<Array<Float>>(arrayOf())

    var roomNumber = MutableLiveData<String>("")
    var bathroomNumber = MutableLiveData<String>("")
    var bedroomNumber = MutableLiveData<String>("")
    var description = MutableLiveData<String>("")
    var schools = MutableLiveData<Boolean>(false)
    var shops = MutableLiveData<Boolean>(false)
    var park = MutableLiveData<Boolean>(false)
    var stations = MutableLiveData<Boolean>(false)
    var hospital = MutableLiveData<Boolean>(false)
    var museum = MutableLiveData<Boolean>(false)

    var sold = MutableLiveData<Boolean>(false)

    var sellDate = MutableLiveData<String>("0")
    var soldDate = MutableLiveData<String>("0")

    var area = MutableLiveData<String>("")

    @ExperimentalCoroutinesApi
    private val propertiesFlow = searchFilterQuery.flatMapLatest {
        searchProperties.invoke(
            it.textQuery
        )
    }

    @ExperimentalCoroutinesApi
    private val propertiesFilteredFlow = searchFilterQuery.flatMapLatest {
        filterSearchProperties.invoke(
            it
        )
    }

    @ExperimentalCoroutinesApi
    val properties = propertiesFlow.asLiveData()


    fun fetchProperties() {
        viewModelScope.launch {
            getProperties.invoke()
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _state.value = DataState.success(it)
                }
        }
    }


    fun filterData() {
        viewModelScope.launch {
            filterSearchProperties.invoke(
                searchFilterQuery.value
            )
                .catch { e ->
                    _stateFilter.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _stateFilter.value = DataState.success(it)
                }
            Timber.d(
                "FILTER_CLICK: ${museum.value}, ${searchQuery.value}, ${typeList.value}, ${museum.value}, ${schools.value}" +
                        "${shops.value}, ${hospital.value}, ${stations.value}, ${park.value}"
            )
        }
    }
}
