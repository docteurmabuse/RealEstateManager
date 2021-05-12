package com.openclassrooms.realestatemanager.db.model.agent

import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class AgentEntityMapper : DomainMapper<AgentEntity, Agent> {
    override fun mapToDomainModel(model: AgentEntity): Agent {
        return Agent(
            id = model.id,
            name = model.name,
            email = model.email,
            photoUrl = model.photoUrl
        )
    }

    override fun mapFromDomainModel(domainModel: Agent): AgentEntity {
        return AgentEntity(
            id = domainModel.id,
            name = domainModel.name,
            email = domainModel.email,
            photoUrl = domainModel.photoUrl
        )
    }

}