package com.openclassrooms.realestatemanager.presentation.ui.agents

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.interactors.agent.GetAgents
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.data.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllAgents: GetAgents
) : ViewModel() {
    private val _state = MutableStateFlow<DataState<List<Agent>>>(DataState.loading(null))
    val state: StateFlow<DataState<List<Agent>>>
        get() = _state
    private val _items: MutableLiveData<List<Agent>> = MutableLiveData<List<Agent>>()
    val items: LiveData<List<Agent>> = _items

    fun fetchAgents() {
        viewModelScope.launch {
            getAllAgents.invoke()
                .catch { e ->
                    _state.value = (DataState.error(e.toString(), null))
                }
                .collectLatest {
                    _state.value = DataState.success(it)
                    _items.value = it
                }
        }
    }
}
