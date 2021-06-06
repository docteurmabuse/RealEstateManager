package com.openclassrooms.realestatemanager.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {
   /* @ViewModelScoped
    @Provides
    fun provideSearchProperties(
        propertyDao: PropertyDao,
        propertyEntityMapper: PropertyEntityMapper
    ): SearchProperties {
        return SearchProperties(
            propertyDao = propertyDao,
            propertyEntityMapper = propertyEntityMapper
        )
    }*/
}
