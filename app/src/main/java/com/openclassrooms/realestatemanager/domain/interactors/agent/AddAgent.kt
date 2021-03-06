package com.openclassrooms.realestatemanager.domain.interactors.agent

import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.repository.AgentRepository_Impl
import javax.inject.Inject

class AddAgent @Inject constructor(private val agentRepositoryImpl: AgentRepository_Impl) {
    suspend operator fun invoke(agent: Agent) {
        agentRepositoryImpl.addAgent(agent)
    }
}