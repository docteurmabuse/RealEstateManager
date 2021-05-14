package com.openclassrooms.realestatemanager.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class PropertyDaoTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var agentDao: AgentDao
    private val agent1 = AgentFactory.makeAgent()

    //Create fake property
    private val property1 = PropertyFactory.makeProperty()
    private val property2 = PropertyFactory.makeProperty()


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
            agentDao.insertAgent(agent1)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        propertyDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAllPropertiesReturnEmptyList() = runBlocking {
        // The list is empty
        //Get first agent in the list
        val firstProperty = propertyDao.getAllProperties().first()
        //First agent size should be 0
        Assert.assertEquals(0, firstProperty.size)
        closeDb()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPropertyAggregate() = runBlocking {
        //Insert fake properties
        propertyDao.insertProperty(property1)
        propertyDao.insertProperty(property2)

        //First Property in database
        val firstProperty = propertyDao.getAllProperties().first()[0]

        //Fake Property 1 should be equal to first agent in database
        assertEquals(property1.property.zipCode, firstProperty.property.zipCode)
        //Fake Property 2 should not be equal to first agent in database
        Assert.assertNotEquals(property2.property.zipCode, firstProperty.property.zipCode)
        closeDb()
    }
}