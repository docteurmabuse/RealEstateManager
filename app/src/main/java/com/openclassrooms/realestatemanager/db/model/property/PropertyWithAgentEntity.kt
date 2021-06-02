package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity

class PropertyWithAgentEntity(
    @Relation(parentColumn = "id", entityColumn = "agent_id")
    val propertyEntity: PropertyEntity,
    @Embedded
    val agent: AgentEntity

) {
    /*companion object {
        fun fromDomain(property: Property): PropertyWithAgentEntity {
            return PropertyWithAgentEntity(
                propertyEntity = PropertyEntity.fromDomain(property),
                agent = AgentEntity.fromDomain(property.agent!!)
            )
        }
    }*/
}
