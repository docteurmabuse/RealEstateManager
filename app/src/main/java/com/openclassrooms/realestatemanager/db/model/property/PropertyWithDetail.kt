package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.model.property.Property

class PropertyWithDetail(
    @Embedded
    val property: PropertyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id",
        entity = PhotoEntity::class
    )
    val photos: List<PhotoEntity> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id",
        entity = VideoEntity::class

    )
    val videos: List<VideoEntity> = arrayListOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id",
        entity = AddressEntity::class

    )
    val address: AddressEntity?
) {


    companion object {
        fun fromDomain(property: Property): PropertyEntityAggregate {
            return PropertyEntityAggregate(

                property = PropertyEntity.fromDomain(property),
                photos = property.media.photos.map {
                    PhotoEntity.fromDomain(property.id!!, it)
                },
                videos = property.media.videos.map {
                    VideoEntity.fromDomain(property.id!!, it)
                },
                address = AddressEntity.fromDomain(property.id!!, property.address)

            )
        }
    }
}
