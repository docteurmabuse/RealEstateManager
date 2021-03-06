package com.openclassrooms.realestatemanager.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.AddressEntity
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity

@Database(
    entities = [
        AgentEntity::class,
        PropertyEntity::class,
        PhotoEntity::class,
        VideoEntity::class,
        AddressEntity::class],
    version = 13
)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun agentDao(): AgentDao

    companion object {
        const val DATABASE_BASE = "property_db"

        @Volatile
        private var INSTANCE: PropertyDatabase? = null
        fun getDatabase(context: Context): PropertyDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PropertyDatabase::class.java,
                        DATABASE_BASE
                    ).build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}
