package com.openclassrooms.realestatemanager.di

import androidx.room.Room
import com.openclassrooms.realestatemanager.BaseApplication
import com.openclassrooms.realestatemanager.data.room.database.PropertyDatabase
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

}