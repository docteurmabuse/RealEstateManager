package com.openclassrooms.realestatemanager.db.model.property

import com.openclassrooms.realestatemanager.domain.model.Media
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class VideoEntityMapper : DomainMapper<VideoEntity, Media.Video> {
    override fun mapToDomainModel(model: VideoEntity): Media.Video {
        return Media.Video(
            id = model.id,
            propertyId = model.propertyId,
            name = model.name,
            videoPath = model.videoPath
        )
    }

    override fun mapFromDomainModel(domainModel: Media.Video): VideoEntity {
        return VideoEntity(
            id = domainModel.id,
            propertyId = domainModel.propertyId,
            name = domainModel.name,
            videoPath = domainModel.videoPath
        )
    }
}