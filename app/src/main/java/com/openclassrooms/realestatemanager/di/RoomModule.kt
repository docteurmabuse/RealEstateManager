package com.openclassrooms.realestatemanager.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.realestatemanager.db.Persistence
import com.openclassrooms.realestatemanager.db.RoomPersistence
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntityMapper
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomModule {

    @Binds
    abstract fun bindPersistence(persistence: RoomPersistence): Persistence

    companion object {

        @Singleton
        @Provides
        fun provideRoomDb(@ApplicationContext context: Context): PropertyDatabase {
            return Room.databaseBuilder(
                context,
                PropertyDatabase::class.java,
                PropertyDatabase.DATABASE_BASE
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        fun providePropertyDao(app: PropertyDatabase): PropertyDao {
            return app.propertyDao()
        }


        @Singleton
        @Provides
        fun provideAgentDao(app: PropertyDatabase): AgentDao {
            return app.agentDao()
        }

        @Singleton
        @Provides
        fun providePropertyEntityMapper(): PropertyEntityMapper {
            return PropertyEntityMapper()
        }

        @Singleton
        @Provides
        fun providesAgentEntityMapper(): AgentEntityMapper {
            return AgentEntityMapper()
        }
    }

}
