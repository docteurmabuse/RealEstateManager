package com.openclassrooms.realestatemanager.db.model.agent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "agent"
)
data class AgentEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var name: String,
    var email: String
)
