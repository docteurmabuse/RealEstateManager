package com.openclassrooms.realestatemanager.domain.interactors.agent

import com.openclassrooms.realestatemanager.repository.AgentRepository_Impl
import javax.inject.Inject

class GetAgentById @Inject constructor(private val agentRepositoryImpl: AgentRepository_Impl) {
    suspend operator fun invoke(id: String) {
        agentRepositoryImpl.getAgentById(id)
    }
}
