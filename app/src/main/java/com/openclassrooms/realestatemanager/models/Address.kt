package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "address_1")
    val address1: Int,
    @ColumnInfo(name = "address_2")
    val address2: String?,
    val city: String = "New York",
    @ColumnInfo(name = "zip_code")
    val zipCode: Int,
    val state: String = "NY",
    val country: String = "United States"
)
