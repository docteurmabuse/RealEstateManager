package com.openclassrooms.realestatemanager.db.model.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.model.Media

@Entity(
    tableName = "estate_videos", foreignKeys = [ForeignKey(
        entity = PropertyEntity::class,
        parentColumns = ["id"],
        childColumns = ["property_id"]
    )]
)
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "property_id", index = true)
    var propertyId: Int,
    var name: String?,
    @ColumnInfo(name = " video_path")
    var videoPath: String
) {

    companion object {
        fun fromDomain(propertyId: Int, video: Media.Video): VideoEntity {
            return VideoEntity(
                propertyId = propertyId,
                name = video.name,
                videoPath = video.videoPath
            )
        }
    }

    fun toDomain(): Media.Video = Media.Video(id, propertyId, name, videoPath)
}
