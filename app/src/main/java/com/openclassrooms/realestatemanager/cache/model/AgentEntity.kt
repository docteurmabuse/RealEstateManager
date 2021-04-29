package com.openclassrooms.realestatemanager.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "agent"
)
data class AgentEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var email: String
)
