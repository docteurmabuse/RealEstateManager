package com.openclassrooms.realestatemanager.presentation.ui

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.interactors.property.GetProperties
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.FilterSearchProperties
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.SearchProperties
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.domain.model.search.SearchFilters
import com.openclassrooms.realestatemanager.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.stream.Collectors
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


    private val Boolean.int
        get() = if (this) 1 else null
    var _filteredPropertyList = MutableLiveData<List<Property>>(arrayListOf())
    var typeList = MutableLiveData<ArrayList<String>>(
        arrayListOf()
    )

    var house = MutableLiveData<Boolean>(true)
    var flat = MutableLiveData<Boolean>(true)
    var duplex = MutableLiveData<Boolean>(true)
    var penthouse = MutableLiveData<Boolean>(true)
    var loft = MutableLiveData<Boolean>(true)
    var manor = MutableLiveData<Boolean>(true)

    var maxPrice = MutableLiveData<Float>(100000000F)
    var minPrice = MutableLiveData<Float>(0F)
    var priceArray = MutableLiveData<Array<Float>>(arrayOf())

    var minSurface = MutableLiveData<Float>(0F)
    var maxSurface = MutableLiveData<Float>(10000F)
    var surfaceArray = MutableLiveData<Array<Float>>(arrayOf())

    var picsNumber = MutableLiveData<Float>(1F)

    var roomNumber = MutableLiveData<String>("")
    var bathroomNumber = MutableLiveData<String>("")
    var bedroomNumber = MutableLiveData<String>("")

    var description = MutableLiveData<String>("")

    var sortBy = MutableLiveData<String>("sell_date")

    var schools = MutableLiveData<Boolean>(false)
    var shops = MutableLiveData<Boolean>(false)
    var park = MutableLiveData<Boolean>(false)
    var stations = MutableLiveData<Boolean>(false)
    var hospital = MutableLiveData<Boolean>(false)
    var museum = MutableLiveData<Boolean>(false)

    var sold = MutableLiveData<Boolean>(false)

    var sellDate = MutableLiveData<String>(DateUtil.todayDate)
    var soldDate = MutableLiveData<String>(DateUtil.todayDate)

    var area = MutableLiveData<String>("")

    init {
        fetchProperties()
    }

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
            types = listOf("House", "Flat", "Duplex", "Penthouse", "Manor", "Loft"),
            minSurface = minSurface.value,
            maxSurface = maxSurface.value,
            minPrice = minPrice.value,
            maxPrice = maxPrice.value,
            sold = null,
            sellDate = null,
            soldDate = null,
            numberOfPics = null,
            rooms = 1,
            beds = 1,
            baths = 1
        )
    )

    @ExperimentalCoroutinesApi
    private var propertiesFlow = searchQuery.flatMapLatest {
        searchProperties.invoke(
            it
        )
    }

    @ExperimentalCoroutinesApi
    private var propertiesFilteredFlow = searchFilterQuery.flatMapLatest {
        filterSearchProperties.invoke(
            it,
            sortBy.value!!
        )
    }

    @ExperimentalCoroutinesApi
    val properties = propertiesFlow.asLiveData()

    @ExperimentalCoroutinesApi
    val filteredPropertyList = propertiesFilteredFlow.asLiveData()


    fun fetchProperties() {
        viewModelScope.launch {
            getProperties.invoke()
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _state.value = DataState.success(it)
                    initSurfaceFilter(it)
                    initPriceFilter(it)
                }
        }
    }

    private fun initPriceFilter(list: List<Property>) {
        minPrice.value =
            list.minWithOrNull(Comparator.comparingInt { it.price!! })?.price?.toFloat()
                ?.minus(100000)
        maxPrice.value =
            list.maxWithOrNull(Comparator.comparingInt { it.price!! })?.price?.toFloat()
        if (minPrice.value != null && maxPrice.value != null)
            priceArray.value = arrayOf(minPrice.value!!, maxPrice.value!!)
    }

    private fun initSurfaceFilter(list: List<Property>) {
        minSurface.value =
            list.minWithOrNull(Comparator.comparingInt { it.surface!! })?.surface?.toFloat()
                ?.minus(10)
        maxSurface.value =
            list.maxWithOrNull(Comparator.comparingInt { it.surface!! })?.surface?.toFloat()
        if (minSurface.value != null && maxSurface.value != null)
            surfaceArray.value = arrayOf(minSurface.value!!, maxSurface.value!!)
    }

    @ExperimentalCoroutinesApi
    fun filterData() {
        if (house.value == true || flat.value == true || duplex.value == true
            || penthouse.value == true || manor.value == true || loft.value == true
        ) {
            typeList.value = arrayListOf()
            if (house.value == true) typeList.value!!.add("House")
            if (flat.value == true) typeList.value!!.add("Flat")
            if (duplex.value == true) typeList.value!!.add("Duplex")
            if (penthouse.value == true) typeList.value!!.add("Penthouse")
            if (loft.value == true) typeList.value!!.add("Loft")
            if (manor.value == true) typeList.value!!.add("Manor")

            Timber.d(
                "FILTER_CLICK: House is true:  ${flat.value}, ${house.value},${typeList.value} "
            )
        }
        val rooms: Int =
            if (roomNumber.value.isNullOrBlank())
                1 else roomNumber.value!!.toInt()

        val beds: Int =
            if (bedroomNumber.value.isNullOrBlank())
                1 else bedroomNumber.value!!.toInt()

        val baths: Int =
            if (bathroomNumber.value.isNullOrBlank())
                1 else bathroomNumber.value!!.toInt()
        searchFilterQuery.value =
            SearchFilters(
                textQuery = searchQuery.value,
                museum = museum.value?.int,
                school = schools.value?.int,
                shop = shops.value?.int,
                hospital = hospital.value?.int,
                station = stations.value?.int,
                park = park.value?.int,
                area = area.value,
                types = typeList.value,
                minSurface = surfaceArray.value?.get(0),
                maxSurface = surfaceArray.value?.get(1),
                minPrice = priceArray.value?.get(0),
                maxPrice = priceArray.value?.get(1),
                sold = sold.value?.int,
                sellDate = sellDate.value?.toLong(),
                soldDate = soldDate.value?.toLong(),
                numberOfPics = picsNumber.value,
                rooms = rooms,
                beds = beds,
                baths = baths
            )

        Timber.d(
            "FILTER_CLICK: House:  ${searchFilterQuery.value}, ${sellDate.value}"
        )
        propertiesFilteredFlow = searchFilterQuery.flatMapLatest {
            filterSearchProperties.invoke(
                searchFilterQuery.value,
                sortBy.value!!
            )
        }


        viewModelScope.launch {
            Timber.d(
                "FILTER_CLICK: park:  ${searchFilterQuery.value}, ${
                    typeList.value?.stream()?.map { it ->
                        it.toString()
                    }?.collect(Collectors.joining("','"))
                }"
            )
            filterSearchProperties.invoke(
                searchFilterQuery.value,
                sortBy.value!!
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
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun resetFilters() {
        house.value = true
        flat.value = true
        duplex.value = true
        penthouse.value = true
        manor.value = true
        priceArray.value = arrayOf(minPrice.value!!, maxPrice.value!!)
        surfaceArray.value = arrayOf(minSurface.value!!, maxSurface.value!!)
        typeList.value = arrayListOf("House", "Flat", "Duplex", "Penthouse", "Manor", "Loft")
        roomNumber.value = "1"
        bathroomNumber.value = "1"
        bedroomNumber.value = "1"
        description.value = ""
        schools.value = false
        shops.value = false
        park.value = false
        stations.value = false
        hospital.value = false
        museum.value = false
        sold.value = false
        sellDate.value = DateUtil.todayDate
        soldDate.value = DateUtil.todayDate
        area.value = ""
        filterData()
    }
}
