package com.openclassrooms.realestatemanager.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.data.room.dao.AgentDao
import com.openclassrooms.realestatemanager.data.room.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.models.Property

@Database(entities = [Property::class, Photo::class, Agent::class], version = 1)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun photoDao(): PhotoDao
    abstract fun agentDao(): AgentDao

    companion object {
        val DATABASE_BASE = "property_db"
    }
}
