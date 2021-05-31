package com.openclassrooms.realestatemanager.db.model.agent

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate

data class AgentEntityWithProperties(
    @Embedded val agent: AgentEntity,
    @Relation(
        entity = PropertyEntity::class,
        parentColumn = "id",
        entityColumn = "agent_id"
    )
    val properties: List<PropertyEntityAggregate>
)
