package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "properties")
data class Property(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val type: String = "House",
        val price: Int,
        val surface: Int,
        @ColumnInfo(name = "room_number")
        val roomNumber: Int,
        val description: String?,
        @ColumnInfo(name = "address_id")
        val addressId: Int,
        val schools: Boolean?,
        val shops: Boolean?,
        val parcs: Boolean?,
        val stations: Boolean?,
        val hospital: Boolean?,
        val museum: Boolean?,
        val sold: Boolean,
        @ColumnInfo(name = "sell_date")
        val sellDate: Date,
        @ColumnInfo(name = "sold_date")
        val soldDate: Date,
        @ColumnInfo(name = "agent_id")
        val agentId: Int
) : Parcelable
