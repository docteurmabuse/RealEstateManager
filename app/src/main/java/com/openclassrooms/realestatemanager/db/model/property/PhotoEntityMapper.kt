package com.openclassrooms.realestatemanager.db.model.property

import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class PhotoEntityMapper : DomainMapper<PhotoEntity, Media.Photo> {

    override fun mapToDomainModel(model: PhotoEntity): Media.Photo {
        return Media.Photo(
            name = model.name,
            photoPath = model.photoPath
        )
    }

    override fun mapFromDomainModel(domainModel: Media.Photo): PhotoEntity {
        TODO("Not yet implemented")
    }
}
