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
                entity = Address::class,
                parentColumns = ["id"],
                childColumns = ["address_id"]
        ),
                ForeignKey(
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
    @ColumnInfo(name = "address_id")
    var addressId: Int,
    var schools: Boolean?,
    var shops: Boolean?,
    var parcs: Boolean?,
    var stations: Boolean?,
    var hospital: Boolean?,
    var museum: Boolean?,
    var sold: Boolean = false,
    @ColumnInfo(name = "sell_date")
    var sellDate: Date,
    @ColumnInfo(name = "sold_date")
    var soldDate: Date,
    @ColumnInfo(name = "agent_id")
    var agentId: Int
) : Parcelable
