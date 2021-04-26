package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estate_photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String?,
    @ColumnInfo(name = "room")
    val room: String,


    )
