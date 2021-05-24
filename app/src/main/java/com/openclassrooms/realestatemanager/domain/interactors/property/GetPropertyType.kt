package com.openclassrooms.realestatemanager.domain.interactors.property

import com.openclassrooms.realestatemanager.domain.model.search.SearchFilters
import com.openclassrooms.realestatemanager.repository.PropertyRepository_Impl
import java.util.*
import javax.inject.Inject

class GetPropertyType @Inject constructor(
    private val propertyRepository: PropertyRepository_Impl
) {
    operator fun invoke(): SearchFilters {
        val types = propertyRepository.getPropertyTypes()
            .map { it.name }
            .map {
                it.lowercase(Locale.ROOT)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            }
        return SearchFilters(types)
    }

    private fun List<String>.replace(old: String, new: String): List<String> {
        return map { if (it == old) new else it }
    }
}