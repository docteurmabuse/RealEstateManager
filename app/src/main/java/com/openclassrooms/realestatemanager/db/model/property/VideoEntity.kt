package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.model.property.Media

@Entity(
    tableName = "property_videos", foreignKeys = [ForeignKey(
        entity = PropertyEntity::class,
        parentColumns = ["id"],
        childColumns = ["property_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    var video_id: Long? = null,
    @ColumnInfo(name = "property_id", index = true)
    var property_id: String,
    var name: String?,
    @ColumnInfo(name = " video_path")
    var videoPath: String
) {

    companion object {
        fun fromDomain(propertyId: String, video: Media.Video): VideoEntity {
            return VideoEntity(
                property_id = propertyId,
                name = video.name,
                videoPath = video.videoPath
            )
        }
    }

    fun toDomain(): Media.Video = Media.Video(name, videoPath)
}
