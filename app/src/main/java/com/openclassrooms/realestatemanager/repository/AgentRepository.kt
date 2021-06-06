package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    suspend fun addAgent(agent: Agent): Long

    suspend fun getAllAgents(): Flow<List<Agent>>

    suspend fun getAgentById(id: String): Flow<Agent>

    suspend fun updateAgent(agent: Agent)
}
