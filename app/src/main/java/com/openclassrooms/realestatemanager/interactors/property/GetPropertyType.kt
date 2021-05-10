package com.openclassrooms.realestatemanager.interactors.property

import com.openclassrooms.realestatemanager.repository.PropertyRepository
import java.util.*
import javax.inject.Inject

class GetPropertyType @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    operator fun invoke(): List<String> {
        return propertyRepository.getPropertyTypes()
            .map { it.name }
            .map { it.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT) }
    }

    private fun List<String>.replace(old: String, new: String): List<String> {
        return map { if (it == old) new else it }
    }
}