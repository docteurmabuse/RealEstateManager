package com.openclassrooms.realestatemanager.db.model.property

import com.openclassrooms.realestatemanager.domain.model.Media
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class PhotoEntityMapper : DomainMapper<PhotoEntity, Media.Photo> {

    override fun mapToDomainModel(model: PhotoEntity): Media.Photo {
        return Media.Photo(
            id = model.id,
            propertyId = model.propertyId,
            name = model.name,
            photoPath = model.photoPath
        )
    }

    override fun mapFromDomainModel(domainModel: Media.Photo): PhotoEntity {
        return PhotoEntity(
            id = domainModel.id,
            propertyId = domainModel.propertyId,
            name = domainModel.name,
            photoPath = domainModel.photoPath
        )
    }
}