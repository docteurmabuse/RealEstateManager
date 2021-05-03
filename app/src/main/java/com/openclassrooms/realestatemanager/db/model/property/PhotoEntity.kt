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
    var id: Int = 0,
    @ColumnInfo(name = "property_id", index = true)
    var propertyId: Int,
    var name: String?,
    @ColumnInfo(name = " photo_path")
    var photoPath: String
) {
    companion object {
        fun fromDomain(propertyId: Int, photo: Media.Photo): PhotoEntity {
            return PhotoEntity(
                propertyId = propertyId,
                name = photo.name,
                photoPath = photo.photoPath
            )
        }
    }

    fun toDomain(): Media.Photo = Media.Photo(id, propertyId, name, photoPath)
}
