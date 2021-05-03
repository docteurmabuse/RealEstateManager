package com.openclassrooms.realestatemanager.db.model.property

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.domain.model.Media
import com.openclassrooms.realestatemanager.domain.model.Property
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
    var id: Int = 0,
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
    var agentId: Int = 1
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
                agentId = domainModel.agentId
            )
        }
    }

    fun toDomain(
        model: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>
    ): Property {
        return Property(
            id = model.id,
            type = Property.Type.valueOf(model.type.toString()),
            price = model.price,
            surface = model.surface,
            roomNumber = model.roomNumber,
            bathroomNumber = model.bathroomNumber,
            bedroomNumber = model.bedroomNumber,
            description = model.description,
            address1 = model.address1,
            address2 = model.address2,
            city = model.city,
            zipCode = model.zipCode,
            state = model.state,
            country = model.country,
            area = model.area,
            schools = model.schools,
            shops = model.shops,
            parcs = model.parcs,
            stations = model.stations,
            hospital = model.hospital,
            museum = model.museum,
            sold = model.sold,
            sellDate = DateUtil.longToDate(model.soldDate),
            soldDate = DateUtil.longToDate(model.soldDate),
            Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            agentId = model.agentId
        )
    }
}
