package com.openclassrooms.realestatemanager.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.cache.dao.*

@Database(
    entities = [PropertyCacheEntity::class, PhotoCacheEntity::class, AgentCacheEntity::class],
    exportSchema = false,
    version = 1
)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun photoDao(): PhotoDao
    abstract fun agentDao(): AgentDao

    companion object {
        const val DATABASE_BASE = "property_db"
    }
}
