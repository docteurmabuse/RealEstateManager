package com.openclassrooms.realestatemanager.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.cache.dao.AgentDao
import com.openclassrooms.realestatemanager.cache.dao.PhotoDao
import com.openclassrooms.realestatemanager.cache.dao.PropertyDao
import com.openclassrooms.realestatemanager.cache.model.AgentEntity
import com.openclassrooms.realestatemanager.cache.model.PhotoEntity
import com.openclassrooms.realestatemanager.cache.model.PropertyEntity

@Database(
    entities = [PropertyEntity::class, PhotoEntity::class, AgentEntity::class],
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
