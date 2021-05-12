package com.openclassrooms.realestatemanager.presentation.ui.property_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.interactors.property.GetProperties
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel
@Inject
constructor(
    //private val searchProperties: SearchProperties,
    private val getProperties: GetProperties,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    //private val _properties: MutableStateFlow<List<Property>> = MutableStateFlow(listOf())
    // val properties: StateFlow<List<Property>>
    //    get() = _properties
    // val query = MutableStateFlow("")
    // private val loading = MutableStateFlow(false)

    val _state = MutableStateFlow<DataState<List<Property>>>(DataState.loading(null))
    val state: StateFlow<DataState<List<Property>>>
        get() = _state

    fun fetchProperties() {
        viewModelScope.launch {
            getProperties.invoke()
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collect {
                    _state.value = DataState.success(it)
                }
        }
    }

    /*   private fun newSearch() {
           searchProperties.execute(query = query.value).onEach { dataState ->
               loading.value = dataState.loading
               dataState.data?.let { list -> _properties.value = list }
           }.launchIn(viewModelScope)
       }*/


}