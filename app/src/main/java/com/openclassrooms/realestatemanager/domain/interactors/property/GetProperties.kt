package com.openclassrooms.realestatemanager.domain.interactors.property

import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import javax.inject.Inject

class GetProperties @Inject constructor(private val propertyRepositoryImpl: PropertyRepository_Impl) {
    suspend operator fun invoke() = propertyRepositoryImpl.getAllProperties()
}