package com.openclassrooms.realestatemanager.cache.model

import com.openclassrooms.realestatemanager.domain.model.Property
import com.openclassrooms.realestatemanager.domain.model.util.DomainMapper
import com.openclassrooms.realestatemanager.utils.DateUtil

class PropertyEntityMapper : DomainMapper<PropertyEntity, Property> {
    override fun mapToDomainModel(model: PropertyEntity): Property {
        return Property(
            id = model.id,
            type = model.type,
            price = model.price,
            surface = model.surface,
            roomNumber = model.roomNumber,
            bathroomNumber = model.bathroomNumber,
            bedroomNumber = model.bedroomNumber,
            description = model.description,
            address1 = model.address1,
            address2 = model.address2,
            city = model.city,
            zipCode = model.zipCode,
            state = model.state,
            country = model.country,
            schools = model.schools,
            shops = model.shops,
            parcs = model.parcs,
            stations = model.stations,
            hospital = model.hospital,
            museum = model.museum,
            sold = model.sold,
            sellDate = DateUtil.longToDate(model.soldDate),
            soldDate = DateUtil.longToDate(model.soldDate),
            agentId = model.agentId
        )
    }

    override fun mapFromDomainModel(domainModel: Property): PropertyEntity {
        return PropertyEntity(
            id = domainModel.id,
            type = domainModel.type,
            price = domainModel.price,
            surface = domainModel.surface,
            roomNumber = domainModel.roomNumber,
            bathroomNumber = domainModel.bathroomNumber,
            bedroomNumber = domainModel.bedroomNumber,
            description = domainModel.description,
            address1 = domainModel.address1,
            address2 = domainModel.address2,
            city = domainModel.city,
            zipCode = domainModel.zipCode,
            state = domainModel.state,
            country = domainModel.country,
            schools = domainModel.schools,
            shops = domainModel.shops,
            parcs = domainModel.parcs,
            stations = domainModel.stations,
            hospital = domainModel.hospital,
            museum = domainModel.museum,
            sold = domainModel.sold,
            sellDate = DateUtil.dateToLong(domainModel.soldDate),
            soldDate = DateUtil.dateToLong(domainModel.soldDate),
            agentId = domainModel.agentId
        )
    }
}