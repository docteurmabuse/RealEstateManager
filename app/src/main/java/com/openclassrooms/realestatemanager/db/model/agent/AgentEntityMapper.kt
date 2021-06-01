package com.openclassrooms.realestatemanager.db.model.agent

import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class AgentEntityMapper : DomainMapper<AgentEntity, Agent> {

    override fun mapToDomainModel(model: AgentEntity): Agent {
        return Agent(
            _id = model.id,
            _name = model.name,
            _email = model.email,
            _phone = model.phone,
            _photoUrl = model.photoUrl
        )
    }

    override fun mapFromDomainModel(domainModel: Agent): AgentEntity {
        return AgentEntity(
            id = domainModel.id,
            name = domainModel.name,
            email = domainModel.email,
            phone = domainModel.phone,
            photoUrl = domainModel.photoUrl
        )
    }

}
