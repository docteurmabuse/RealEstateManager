package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import com.openclassrooms.realestatemanager.domain.model.Property
import com.openclassrooms.realestatemanager.utils.DispatchersProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class PropertyRepository_Impl @Inject constructor(
    private val persistence: Persistence,
    dispatchersProvider: DispatchersProvider
) : PropertyRepository {

    override suspend fun addProperty(property: Property) {
        persistence.storeProperty(PropertyEntityAggregate.fromDomain(property))
    }

    override suspend fun searchProperty(query: String): List<Property> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProperties(): Flow<List<Property>> {
        return persistence.getAllProperties()
            .distinctUntilChanged()
            .map { propertyList ->
                propertyList.map { it.property.toDomain(it.photos, it.videos) }
            }
    }

    override suspend fun getPropertyById(id: Int): Property {
        TODO("Not yet implemented")
    }

    override suspend fun updateProperty(property: Property) {
        TODO("Not yet implemented")
    }


}