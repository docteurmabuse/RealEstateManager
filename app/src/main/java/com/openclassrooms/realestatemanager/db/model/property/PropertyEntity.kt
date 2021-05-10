package com.openclassrooms.realestatemanager.db.model.property

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.utils.DateUtil
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "properties",
    foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["id"],
        childColumns = ["agent_id"]
    )],
)
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var type: String?,
    var price: Int?,
    var surface: Int?,
    @ColumnInfo(name = "room_number")
    var roomNumber: Int?,
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
    var state: String? = "NY",
    var country: String = "United States",
    var area: String?,
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
    @ColumnInfo(name = "agent_id", index = true)
    var agent_id: Long = 1
) : Parcelable {
    companion object {
        fun fromDomain(domainModel: Property): PropertyEntity {
            return PropertyEntity(
                id = domainModel.id,
                type = domainModel.type.toString(),
                price = domainModel.price,
                surface = domainModel.surface,
                roomNumber = domainModel.roomNumber,
                bathroomNumber = domainModel.bathroomNumber,
                bedroomNumber = domainModel.bedroomNumber,
                description = domainModel.description,
                address1 = domainModel.address1,
                address2 = domainModel.address2,
                city = domainModel.city,
                zipCode = domainModel.zipCode,
                state = domainModel.state,
                country = domainModel.country,
                area = domainModel.area,
                schools = domainModel.schools,
                shops = domainModel.shops,
                parcs = domainModel.parcs,
                stations = domainModel.stations,
                hospital = domainModel.hospital,
                museum = domainModel.museum,
                sold = domainModel.sold,
                sellDate = DateUtil.dateToLong(domainModel.soldDate),
                soldDate = DateUtil.dateToLong(domainModel.soldDate),
                agent_id = domainModel.agentId
            )
        }
    }

    fun toDomain(
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>
    ): Property {
        return Property(
            id = id,
            type = Property.PropertyType.valueOf(type.toString()),
            price = price,
            surface = surface,
            roomNumber = roomNumber,
            bathroomNumber = bathroomNumber,
            bedroomNumber = bedroomNumber,
            description = description,
            address1 = address1,
            address2 = address2,
            city = city,
            zipCode = zipCode,
            state = state,
            country = country,
            area = area,
            schools = schools,
            shops = shops,
            parcs = parcs,
            stations = stations,
            hospital = hospital,
            museum = museum,
            sold = sold,
            sellDate = DateUtil.longToDate(soldDate),
            soldDate = DateUtil.longToDate(soldDate),
            Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            agentId = agent_id
        )
    }
}
