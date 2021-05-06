package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.model.Media

@Entity(
    tableName = "estate_photos", foreignKeys = [ForeignKey(
        entity = PropertyEntity::class,
        parentColumns = ["id"],
        childColumns = ["property_id"]
    )]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "property_id", index = true)
    var propertyId: Long,
    var name: String?,
    @ColumnInfo(name = " photo_path")
    var photoPath: String
) {
    companion object {
        fun fromDomain(propertyId: Long, photo: Media.Photo): PhotoEntity {
            return PhotoEntity(
                propertyId = propertyId,
                name = photo.name,
                photoPath = photo.photoPath
            )
        }
    }

    fun toDomain(): Media.Photo = Media.Photo(name, photoPath)
}
