package com.openclassrooms.realestatemanager.models

import androidx.room.Entity

@Entity(
    tableName = "agent"
)
data class Agent(
    var id: Int,
    var name: String,
    var email: String
)
