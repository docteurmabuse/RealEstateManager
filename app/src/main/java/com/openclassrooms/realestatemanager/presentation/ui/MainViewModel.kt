package com.openclassrooms.realestatemanager.presentation.ui

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.interactors.property.GetProperties
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.SearchProperties
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getProperties: GetProperties,
    private val searchProperties: SearchProperties,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<List<Property>>>(DataState.loading(null))
    val state: StateFlow<DataState<List<Property>>>
        get() = _state
    val searchQuery = MutableStateFlow("")
    var arrStr = enumValues<Property.PropertyType>().toList() as List<String>
    var typeList = MutableLiveData<List<String>>(arrStr)
    var house = MutableLiveData<Boolean>(false)
    var flat = MutableLiveData<Boolean>(false)
    var duplex = MutableLiveData<Boolean>(false)
    var penthouse = MutableLiveData<Boolean>(false)
    var manor = MutableLiveData<Boolean>(false)

    var price = MutableLiveData<String>("")
    var surface = MutableLiveData<String>("")
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

    @ExperimentalCoroutinesApi
    private val propertiesFlow = searchQuery.flatMapLatest {
        searchProperties.invoke(it, typeList.value)
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
}
