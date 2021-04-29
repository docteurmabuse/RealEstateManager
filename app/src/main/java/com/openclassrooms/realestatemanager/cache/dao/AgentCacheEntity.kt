package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "agent"
)
data class AgentCacheEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var email: String
)
