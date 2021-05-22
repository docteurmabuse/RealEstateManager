package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "property_id", index = true)
    var property_id: Long,
    var lat: String?,
    @ColumnInfo(name = " video_path")
    var lng: String
)
