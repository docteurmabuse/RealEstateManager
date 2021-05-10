package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val intentChannel = Channel<Intent>(Channel.UNLIMITED)
    val _state = MutableStateFlow(DataState<List<Property>>())
    val state: StateFlow<DataState<List<Property>>>
        get() = _state
}
