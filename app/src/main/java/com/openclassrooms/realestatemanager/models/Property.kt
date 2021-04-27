package com.openclassrooms.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "properties",
    foreignKeys = [ForeignKey(
        entity = Agent::class,
        parentColumns = ["id"],
        childColumns = ["agent_id"]
    )],
)
data class Property(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var type: String = "House",
    var price: Int,
    var surface: Int,
    @ColumnInfo(name = "room_number")
    var roomNumber: Int,
    @ColumnInfo(name = "bathroom_number")
    var bathroomNumber: Int?,
    @ColumnInfo(name = "bedroom_number")
    var bedroomNumber: Int?,
    var description: String?,
    @ColumnInfo(name = "address_1")
    var address1: String,
    @ColumnInfo(name = "address_2")
    var address2: String?,
    var city: String = "New York",
    @ColumnInfo(name = "zip_code")
    var zipCode: Int,
    var state: String = "NY",
    var country: String = "United States",
    var schools: Boolean?,
    var shops: Boolean?,
    var parcs: Boolean?,
    var stations: Boolean?,
    var hospital: Boolean?,
    var museum: Boolean?,
    var sold: Boolean = false,
    @ColumnInfo(name = "sell_date")
    var sellDate: Long,
    @ColumnInfo(name = "sold_date")
    var soldDate: Long,
    @ColumnInfo(name = "agent_id")
    var agentId: Int
) : Parcelable
