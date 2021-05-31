package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.domain.model.property.Property

class PropertyWithAgentEntity(
    @Embedded
    val propertyEntityAggregate: PropertyEntityAggregate,

    @Relation(parentColumn = "agent_id", entityColumn = "id")
    val agent: AgentEntity

) {
    companion object {
        fun fromDomain(property: Property): PropertyWithAgentEntity {
            return PropertyWithAgentEntity(
                propertyEntityAggregate = PropertyEntityAggregate.fromDomain(property),
                agent = AgentEntity.fromDomain(property.agent!!)
            )
        }
    }
}
