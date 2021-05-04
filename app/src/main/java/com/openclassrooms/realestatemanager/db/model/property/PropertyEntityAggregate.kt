package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.domain.model.Property


class PropertyEntityAggregate(
    @Embedded
    val property: PropertyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val photos: List<PhotoEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val videos: List<VideoEntity>,
) {
    companion object {
        fun fromDomain(property: Property): PropertyEntityAggregate {
            return PropertyEntityAggregate(
                property = PropertyEntity.fromDomain(property),
                photos = property.media.photos.map {
                    PhotoEntity.fromDomain(property.id, it)
                },
                videos = property.media.videos.map {
                    VideoEntity.fromDomain(property.id, it)
                }
            )
        }
    }
}