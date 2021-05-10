package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityMapper
import com.openclassrooms.realestatemanager.domain.interactors.property.SearchProperties
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {
    @ViewModelScoped
    @Provides
    fun provideSearchProperties(
        propertyDao: PropertyDao,
        propertyEntityMapper: PropertyEntityMapper
    ): SearchProperties {
        return SearchProperties(
            propertyDao = propertyDao,
            propertyEntityMapper = propertyEntityMapper
        )
    }

}