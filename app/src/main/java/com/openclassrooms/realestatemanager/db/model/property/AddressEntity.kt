package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.model.property.Address

@Entity(
    tableName = "estate_address", foreignKeys = [ForeignKey(
        entity = PropertyEntity::class,
        parentColumns = ["id"],
        childColumns = ["property_id"]
    )]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "property_id", index = true)
    var property_id: Long,
    var address1: String? = "",
    @ColumnInfo(name = "address_2")
    var address2: String? = "",
    var city: String? = "New York",
    @ColumnInfo(name = "zip_code")
    var zipCode: Int? = null,
    var state: String? = "NY",
    var country: String? = "United States",
    var area: String?,
    var lat: Double?,
    var lng: Double?
) {
    companion object {
        fun fromDomain(propertyId: Long, address: Address?): AddressEntity {
            return AddressEntity(
                property_id = propertyId,
                address1 = address?.address1,
                address2 = address?.address2,
                city = address?.city,
                zipCode = address?.zipCode,
                state = address?.state,
                country = address?.country,
                area = address?.area,
                lat = address?.lat,
                lng = address?.lng
            )
        }
    }

    fun toDomain(): Address = Address(
        address1,
        address2,
        city,
        zipCode,
        state,
        country,
        area,
        lat,
        lng
    )
}
