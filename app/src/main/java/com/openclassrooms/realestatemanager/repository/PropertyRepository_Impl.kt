package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.model.property.PropertyWithAgentEntity
import com.openclassrooms.realestatemanager.domain.model.property.Property
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
        persistence.storeProperty(PropertyWithAgentEntity.fromDomain(property))
    }

    override suspend fun searchProperty(query: String): Flow<List<Property>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProperties(): Flow<List<Property>> {
        return persistence.getAllProperties()
            .distinctUntilChanged()
            .map { propertyList ->
                propertyList.map {
                    it.propertyEntityAggregate.property.toDomain(
                        it.propertyEntityAggregate.photos,
                        it.propertyEntityAggregate.videos,
                        it.propertyEntityAggregate.address,
                        it.agent
                    )
                }
                // propertyList.map { it.property.toDomain(it.photos, it.videos, it.address) }
            }
    }

    override fun getPropertyTypes(): List<Property.PropertyType> {
        return getPropertyTypes()
    }

    override suspend fun getPropertyById(id: String): Flow<Property> {
        return persistence.getPropertyById(id)
            .distinctUntilChanged()
            .map { it ->
                it.propertyEntityAggregate.property.toDomain(
                    it.propertyEntityAggregate.photos,
                    it.propertyEntityAggregate.videos,
                    it.propertyEntityAggregate.address,
                    it.agent
                )
            }
    }

    override suspend fun updateProperty(property: Property) {
        persistence.updateProperty(PropertyWithAgentEntity.fromDomain(property))
    }


}
