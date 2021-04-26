package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "address_1")
    var address1: String,
    @ColumnInfo(name = "address_2")
    var address2: String?,
    var city: String = "New York",
    @ColumnInfo(name = "zip_code")
    var zipCode: Int,
    var state: String = "NY",
    var country: String = "United States"
)
