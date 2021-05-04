package com.openclassrooms.realestatemanager.interactors.property

import com.openclassrooms.realestatemanager.domain.model.Property
import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import javax.inject.Inject

class getPropertyType @Inject constructor(
    private val propertyRepositoryImpl: PropertyRepository_Impl
) {
    suspend operator fun invoke(): List<Property.Type> {
        return propertyRepositoryImpl.getPropertyTypes()
    }
}