package com.openclassrooms.realestatemanager.presentation.ui.addAgent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.interactors.agent.AddAgent
import com.openclassrooms.realestatemanager.domain.interactors.agent.GetAgentById
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import com.openclassrooms.realestatemanager.presentation.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel

class AddEditAgentViewModel @Inject constructor(
    val addAgent: AddAgent,
    val getAgentById: GetAgentById
) : ViewModel() {

    // Two-way databinding, exposing MutableLiveData
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()

    private val _state =
        MutableStateFlow<DataState<Agent>>(DataState.loading(null))
    val state: StateFlow<DataState<Agent>>
        get() = _state

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _agentUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: LiveData<Event<Unit>> = _agentUpdatedEvent

    private var agentId: String? = null

    private var isNewAgent: Boolean = false

    private var isDataLoaded = false

    private var addAgentCompleted = false

    fun start(agentId: String?) {
        if (_dataLoading.value == true) {
            return
        }
        this.agentId = agentId
        if (agentId == null) {
            // No need to populate, it's a new agent
            isNewAgent = true
            return
        }
        if (isDataLoaded) {
            // No need to populate, already have data.
            return
        }
        isNewAgent = false
        _dataLoading.value = true
        viewModelScope.launch {
            getAgentById.invoke(agentId).let {

            }
        }
    }

    fun saveAgent() {
        val currentName = name.value
        val currentPhone = phone.value
        val currentEmail = email.value
        val currentImageUrl = imageUrl.value
        val currentAgentId = agentId

        if (currentEmail == null || currentName == null || currentPhone == null) {
            _snackbarText.value = Event(R.string.empty_agent_message)
            return
        }
        if (Agent(
                currentAgentId,
                currentName,
                currentPhone,
                currentEmail,
                currentImageUrl
            ).isEmpty
        ) {
            _snackbarText.value = Event(R.string.empty_agent_message)
            return
        }
        if (isNewAgent || currentAgentId == null) {
            createAgent(
                Agent(
                    currentAgentId,
                    currentName,
                    currentPhone,
                    currentEmail,
                    currentImageUrl
                )
            )
        }
    }

    private fun createAgent(agent: Agent) {
        viewModelScope.launch {
            Timber.tag("FabClick").d("It's ok AGENT: $agent")
            addAgent.invoke(agent)
            _agentUpdatedEvent.value = Event(Unit)
        }
    }

}
