package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "estate_photos", foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = ["id"],
        childColumns = ["property_id"]
    )]
)
data class Photo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "property_id")
    var propertyId: Int,
    var name: String?,
    @ColumnInfo(name = "room")
    var room: String,
    @ColumnInfo(name = " photo_path")
    var photoPath: String
)
