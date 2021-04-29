package com.openclassrooms.realestatemanager.cache.model

import com.openclassrooms.realestatemanager.domain.model.Photo
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class PhotoEntityMapper : DomainMapper<PhotoEntity, Photo> {

    override fun mapToDomainModel(model: PhotoEntity): Photo {
        return Photo(
            id = model.id,
            propertyId = model.propertyId,
            name = model.name,
            room = model.room,
            photoPath = model.photoPath
        )
    }

    override fun mapFromDomainModel(domainModel: Photo): PhotoEntity {
        return PhotoEntity(
            id = domainModel.id,
            propertyId = domainModel.propertyId,
            name = domainModel.name,
            room = domainModel.room,
            photoPath = domainModel.photoPath
        )
    }
}