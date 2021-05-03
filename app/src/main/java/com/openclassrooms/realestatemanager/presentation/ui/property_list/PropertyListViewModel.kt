package com.openclassrooms.realestatemanager.presentation.ui.property_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.model.Property
import com.openclassrooms.realestatemanager.interactors.property.SearchProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PropertyListViewModel
@Inject
constructor(
    private val searchProperties: SearchProperties,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _properties: MutableStateFlow<List<Property>> = MutableStateFlow(listOf())
    val properties: StateFlow<List<Property>>
        get() = _properties
    val query = MutableStateFlow<String>("")
    private val loading = MutableStateFlow<Boolean>(false)

    private fun newSearch() {
        searchProperties.execute(query = query.value).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list -> _properties.value = list }
        }.launchIn(viewModelScope)
    }


}