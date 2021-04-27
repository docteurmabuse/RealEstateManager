package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "agent"
)
data class Agent(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var name: String,
    var email: String
)
