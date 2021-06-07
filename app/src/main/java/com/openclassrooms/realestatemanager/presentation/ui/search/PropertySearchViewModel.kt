package com.openclassrooms.realestatemanager.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.interactors.searchProperty.SearchProperties
import javax.inject.Inject

class PropertySearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchProperties: SearchProperties?
) : ViewModel()
