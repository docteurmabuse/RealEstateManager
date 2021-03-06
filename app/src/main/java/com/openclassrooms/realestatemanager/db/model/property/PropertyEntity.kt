package com.openclassrooms.realestatemanager.db.model.property

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "properties",
    foreignKeys = [ForeignKey(
        entity = AgentEntity::class,
        parentColumns = ["id"],
        childColumns = ["agent_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PropertyEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var type: String?,
    var price: Int?,
    var surface: Int?,
    @ColumnInfo(name = "room_number")
    var roomNumber: Int?,
    @ColumnInfo(name = "bathroom_number")
    var bathroomNumber: Int?,
    @ColumnInfo(name = "bedroom_number")
    var bedroomNumber: Int? = null,
    var description: String? = "",
    var schools: Boolean? = false,
    var shops: Boolean? = false,
    var park: Boolean? = false,
    var stations: Boolean? = false,
    var hospital: Boolean? = false,
    var museum: Boolean? = false,
    var sold: Boolean? = false,
    @ColumnInfo(name = "sell_date")
    var sellDate: Long? = 0,
    @ColumnInfo(name = "sold_date")
    var soldDate: Long? = 0,
    @ColumnInfo(name = "agent_id", index = true)
    var agent_id: String? = ""
) : Parcelable {
    companion object {
        fun fromDomain(domainModel: Property): PropertyEntity {
            return PropertyEntity(
                id = domainModel.id!!,
                type = domainModel.type.toString(),
                price = domainModel.price,
                surface = domainModel.surface,
                roomNumber = domainModel.roomNumber,
                bathroomNumber = domainModel.bathroomNumber,
                bedroomNumber = domainModel.bedroomNumber,
                description = domainModel.description,
                schools = domainModel.schools,
                shops = domainModel.shops,
                park = domainModel.park,
                stations = domainModel.stations,
                hospital = domainModel.hospital,
                museum = domainModel.museum,
                sold = domainModel.sold,
                sellDate = domainModel.sellDate,
                soldDate = domainModel.soldDate,
                agent_id = domainModel.agent
            )
        }
    }

    fun toDomain(
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity
    ): Property {
        return Property(
            _id = id,
            _type = type,
            _price = price,
            _surface = surface,
            _roomNumber = roomNumber,
            _bathroomNumber = bathroomNumber,
            _bedroomNumber = bedroomNumber,
            _description = description,
            _schools = schools,
            _shops = shops,
            _park = park,
            _stations = stations,
            _hospital = hospital,
            _museum = museum,
            _sold = sold,
            _sellDate = sellDate,
            _soldDate = soldDate,
            Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            _agent = agent_id,
            _address = address.toDomain()
        )
    }
}
