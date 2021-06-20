package com.openclassrooms.realestatemanager.provider

import android.database.Cursor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.data.PropertyFactory
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PropertyContentProviderTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var agentDao: AgentDao

    //Create fake property
    private val property1 = PropertyFactory.makeProperty()
    private val property2 = PropertyFactory.makeProperty()
    private val property = PropertyFactory.makeOneProperty()
    private val propertyUpdate = PropertyFactory.makeOneUpdateProperty()

    private val agent =
        AgentEntity("1", "John Wayne", "john45@gmail.com", "121221", "myphotourl.com")

    @Before
    fun initDb() {
        propertyDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        ).build()
        propertyDao = propertyDatabase.propertyDao()
        agentDao = propertyDatabase.agentDao()

        runBlocking {
            //Insert fake agents before the Property for Foreign Key purpose
            agentDao.insertAgent(agent)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        propertyDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAllPropertiesWithCursor() {
        runBlocking {
            //Insert fake properties
            propertyDao.insertProperty(property1)
            propertyDao.insertProperty(property2)
            propertyDao.insertProperty(property2)

            val cursor: Cursor = propertyDao.getAllPropertiesWithCursor()
            val propertiesDbCount = propertyDao.getPropertiesCount()
            assertEquals(propertiesDbCount, cursor.count)
        }
    }
}
