package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.agent.Agent

interface AgentRepository {
    suspend fun addAgent(agent: Agent)

}