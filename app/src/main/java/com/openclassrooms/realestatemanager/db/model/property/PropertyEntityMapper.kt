package com.openclassrooms.realestatemanager.db.model.property

import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper

class PropertyEntityMapper : DomainMapper<PropertyEntity, Property> {
    fun mapToDomainModel(
        model: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity,
    ): Property {
        return Property(
            _id = model.id,
            _type = model.type,
            _price = model.price,
            _surface = model.surface,
            _roomNumber = model.roomNumber,
            _bathroomNumber = model.bathroomNumber,
            _bedroomNumber = model.bedroomNumber,
            _description = model.description,
            _schools = model.schools,
            _shops = model.shops,
            _park = model.park,
            _stations = model.stations,
            _hospital = model.hospital,
            _museum = model.museum,
            _sold = model.sold,
            _sellDate = model.soldDate,
            _soldDate = model.soldDate,
            Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            _agent = model.agent_id,
            _address = address.toDomain()
        )
    }

    override fun mapFromDomainModel(domainModel: Property): PropertyEntity {
        return PropertyEntity(
            id = domainModel.id!!,
            type = domainModel.type.toString(),
            price = domainModel.price,
            surface = domainModel.surface,
            roomNumber = domainModel.roomNumber,
            bathroomNumber = domainModel.bathroomNumber,
            bedroomNumber = domainModel.bedroomNumber,
            description = domainModel.description,
            schools = domainModel.schools,
            shops = domainModel.shops,
            park = domainModel.park,
            stations = domainModel.stations,
            hospital = domainModel.hospital,
            museum = domainModel.museum,
            sold = domainModel.sold,
            sellDate = domainModel.soldDate,
            soldDate = domainModel.soldDate,
            agent_id = domainModel.agent
        )
    }

    fun fromPropertyEntityList(initial: List<PropertyEntity>): List<Property> {
        return initial.map { mapToDomainModel(it) }
    }

    fun toPropertyEntityList(initial: List<Property>): List<PropertyEntity> {
        return initial.map { mapFromDomainModel(it) }
    }

    override fun mapToDomainModel(model: PropertyEntity): Property {
        TODO("Not yet implemented")
    }


}
