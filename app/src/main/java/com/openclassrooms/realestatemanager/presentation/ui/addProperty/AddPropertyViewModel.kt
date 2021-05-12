package com.openclassrooms.realestatemanager.presentation.ui.addProperty

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.domain.interactors.property.AddProperty
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addProperty: AddProperty
) : ViewModel() {
    fun addPropertyToRoomDb(property: Property) {
        Timber.tag("FabClick").d("It's ok FAB2")

        viewModelScope.launch {
            val currentAgentId = FirebaseAuth.getInstance().currentUser.uid
            property.agentId = currentAgentId
            Timber.d("PROPERTY: ${property.agentId}, ${property.address1}")

            addProperty.invoke(property)
        }
    }
}

