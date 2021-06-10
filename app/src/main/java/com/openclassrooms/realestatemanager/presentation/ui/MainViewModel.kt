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
    private var searchFilterQuery = MutableStateFlow(
        SearchFilters(
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            "",
            arrayListOf(),
        )
    )

    private val Boolean.int
        get() = if (this) 1 else null
    var _filteredPropertyList = MutableLiveData<List<Property>>(arrayListOf())
    var filteredPropertyList: LiveData<List<Property>> = _filteredPropertyList
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
    private var propertiesFlow = searchQuery.flatMapLatest {
        searchProperties.invoke(
            it
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


    @ExperimentalCoroutinesApi
    fun filterData() {
        searchFilterQuery.value =
            SearchFilters(
                searchQuery.value,
                museum.value?.int,
                schools.value?.int,
                shops.value?.int,
                hospital.value?.int,
                stations.value?.int,
                park.value?.int,
                area.value,
                types = typeList.value,
            )



        Timber.d(
            "FILTER_CLICK: park:  ${filteredPropertyList.value}"
        )
        /* propertiesFlow=searchFilterQuery.flatMapLatest {
             filterSearchProperties.invoke(
                 searchFilterQuery.value
             )
         }*/


        viewModelScope.launch {
            /* _filteredPropertyList.value = filteredPropertyList.value?.filter {

                  it.park == park.value

              }*/
            _filteredPropertyList.value = getPropertiesMatchFilter(_filteredPropertyList.value)

            Timber.d(
                "FILTER_CLICK: park:  ${getPropertiesMatchFilter(_filteredPropertyList.value)}, ${park.value}"
            )
            /* filterSearchProperties.invoke(
                 searchFilterQuery.value
             )
                 .catch { e ->
                     _stateFilter.value = (DataState.error(e.toString(), null))
                     Timber.d(
                         "FILTER_CLICK: $e"
                     )
                 }
                 .collectLatest {
                     _stateFilter.value = DataState.success(it)
                     Timber.d(
                         "FILTER_CLICK_RESULT: $it"
                     )
                 }*/
        }
    }

    private fun getPropertiesMatchFilter(list: List<Property>?): List<Property>? {
        val conditions = ArrayList<(Property) -> Boolean>()
        if (park.value == true) {
            conditions.add { it.park == true }
        }
        if (museum.value == true) {
            conditions.add { it.museum == true }
        }
        return if (list != null) {
            list.filter { property -> conditions.all { it(property) } }
        } else list
    }
}
