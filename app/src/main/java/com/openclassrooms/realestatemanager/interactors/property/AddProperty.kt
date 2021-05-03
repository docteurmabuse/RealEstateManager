package com.openclassrooms.realestatemanager.interactors.property

import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityMapper

class AddProperty(
    private val propertyDao: PropertyDao,
    private val propertyEntityMapper: PropertyEntityMapper
) {
    //  suspend operator fun invoke(property: Property) =
    //propertyDao.insertProperty(propertyEntityMapper.mapFromDomainModel(property))
}