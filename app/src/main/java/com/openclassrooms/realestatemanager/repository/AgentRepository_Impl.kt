package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntityMapper
import com.openclassrooms.realestatemanager.domain.model.Agent
import com.openclassrooms.realestatemanager.utils.DispatchersProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
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
}