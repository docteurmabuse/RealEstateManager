package com.openclassrooms.realestatemanager.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PhotoDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity

@Database(
    entities = [PropertyEntity::class, PhotoEntity::class, VideoEntity::class, AgentEntity::class],
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
