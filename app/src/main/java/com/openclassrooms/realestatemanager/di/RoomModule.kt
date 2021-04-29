package com.openclassrooms.realestatemanager.di

import androidx.room.Room
import com.openclassrooms.realestatemanager.cache.dao.AgentDao
import com.openclassrooms.realestatemanager.cache.dao.PhotoDao
import com.openclassrooms.realestatemanager.cache.dao.PropertyDao
import com.openclassrooms.realestatemanager.cache.database.PropertyDatabase
import com.openclassrooms.realestatemanager.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRoomDb(app: BaseApplication): PropertyDatabase {
        return Room.databaseBuilder(
            app,
            PropertyDatabase::class.java,
            PropertyDatabase.DATABASE_BASE
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesPropertyDao(app: PropertyDatabase): PropertyDao {
        return app.propertyDao()
    }

    @Singleton
    @Provides
    fun providesPhotoDao(app: PropertyDatabase): PhotoDao {
        return app.photoDao()
    }

    @Singleton
    @Provides
    fun providesAgentDao(app: PropertyDatabase): AgentDao {
        return app.agentDao()
    }
}