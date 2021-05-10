package com.openclassrooms.realestatemanager.domain.interactors.property

import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import javax.inject.Inject

class AddProperty @Inject constructor(
    private val propertyRepositoryImpl: PropertyRepository_Impl
) {
    suspend operator fun invoke(property: Property) = propertyRepositoryImpl.addProperty(property)
}