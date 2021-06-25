package com.openclassrooms.realestatemanager.presentation.ui.property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.interactors.agent.GetAgentById
import com.openclassrooms.realestatemanager.domain.interactors.property.GetPropertyById
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.domain.model.property.Property
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    val getAgentById: GetAgentById,
    val getPropertyById: GetPropertyById
) : ViewModel() {

    private val _state = MutableStateFlow<DataState<Property>>(DataState.loading(null))
    val propertyState: StateFlow<DataState<Property>>
        get() = _state

    private val _stateAgent = MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val agentState: StateFlow<DataState<Agent>>
        get() = _stateAgent


    fun getAgent(agentId: String) {
        viewModelScope.launch {
            agentId.let { it ->
                getAgentById.invoke(it).catch { e ->
                    _stateAgent.value = (DataState.error(e.toString(), null))
                }
                    .collectLatest {
                        _stateAgent.value = DataState.success(it)
                    }
            }
        }
    }

    fun start(propertyId: String) {
        viewModelScope.launch {
            getPropertyById.invoke(propertyId)
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _state.value = DataState.success(it)
                }
        }
    }
}
