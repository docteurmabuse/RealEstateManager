package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntityMapper
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.utils.DispatchersProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class AgentRepository_Impl @Inject constructor(
    private val agentEntityMapper: AgentEntityMapper,
    private val persistence: Persistence,
    dispatchersProvider: DispatchersProvider
) : AgentRepository {
    override suspend fun addAgent(agent: Agent) {
        persistence.storeAgent(agentEntityMapper.mapFromDomainModel(agent))
    }

    override suspend fun getAgentById(id: String): Flow<Agent> {
        return persistence.getAgentById(id)
            .distinctUntilChanged()
            .map {
                it.toDomain()
            }
    }

    override suspend fun updateAgent(agent: Agent) {
        TODO("Not yet implemented")
    }
}
