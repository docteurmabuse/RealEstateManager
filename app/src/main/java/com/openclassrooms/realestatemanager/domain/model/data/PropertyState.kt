package com.openclassrooms.realestatemanager.domain.model.data

import com.openclassrooms.realestatemanager.domain.model.property.Property
import com.openclassrooms.realestatemanager.presentation.Event

data class PropertyState(
    val loading: Boolean = true,
    val properties: List<Property> = emptyList(),
    val failure: Event<Throwable>? = null
)