package com.openclassrooms.realestatemanager.db.model.property

import com.openclassrooms.realestatemanager.domain.model.property.Address
import com.openclassrooms.realestatemanager.domain.model.property.Media
import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper
import com.openclassrooms.realestatemanager.utils.DateUtil

class PropertyEntityMapper : DomainMapper<PropertyEntity, Property> {
    fun mapToDomainModel(
        model: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: Address
    ): Property {
        return Property(
            id = model.id,
            type = model.type,
            price = model.price,
            surface = model.surface,
            roomNumber = model.roomNumber,
            bathroomNumber = model.bathroomNumber,
            bedroomNumber = model.bedroomNumber,
            description = model.description,
            schools = model.schools,
            shops = model.shops,
            park = model.park,
            stations = model.stations,
            hospital = model.hospital,
            museum = model.museum,
            sold = model.sold,
            sellDate = DateUtil.longToDate(model.soldDate),
            soldDate = DateUtil.longToDate(model.soldDate),
            Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            agentId = model.agent_id,
            address = address
        )
    }

    override fun mapFromDomainModel(domainModel: Property): PropertyEntity {
        return PropertyEntity(
            id = domainModel.id,
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
            sellDate = DateUtil.dateToLong(domainModel.soldDate),
            soldDate = DateUtil.dateToLong(domainModel.soldDate),
            agent_id = domainModel.agentId
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