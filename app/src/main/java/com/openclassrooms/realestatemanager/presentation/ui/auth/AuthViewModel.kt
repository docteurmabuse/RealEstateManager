package com.openclassrooms.realestatemanager.presentation.ui.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.interactors.agent.AddAgent
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private var addAgent: AddAgent
) : ViewModel() {
    val _state = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val state: StateFlow<DataState<Agent>>
        get() = _state

    fun addAgentToRoomDb(agent: Agent) =
        viewModelScope.launch {
            addAgent.invoke(agent)
        }
}