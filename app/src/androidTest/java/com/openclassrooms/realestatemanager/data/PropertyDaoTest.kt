package com.openclassrooms.realestatemanager.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
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

    //Create fake property
    private val property1 = PropertyFactory.makeProperty()
    private val property2 = PropertyFactory.makeProperty()
    private val property = PropertyFactory.makeOneProperty()
    private val propertyUpdate = PropertyFactory.makeOneUpdateProperty()

    private val agent = AgentEntity("1", "John Wayne", "kjjk", "121221", "ddd")


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
        assertEquals(property1.address.zipCode, firstProperty.address.zipCode)
        //Fake Property 2 should not be equal to first agent in database
        Assert.assertNotEquals(property2.address.zipCode, firstProperty.address.zipCode)
        closeDb()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAndUpdatePropertyAggregate() = runBlocking {
        //Insert fake properties
        propertyDao.insertProperty(property)

        //First Property in database
        var firstProperty = propertyDao.getAllProperties().first()[0]

        //Fake Property 1 should be equal to first agent in database
        assertEquals(property.property.price, firstProperty.property.price)
        //Fake Property 1 should be equal to first agent in database
        assertEquals(property.property.id, firstProperty.property.id)
        assertEquals("1", firstProperty.property.id)

        assertNotSame(10000, firstProperty.property.price)
        //Update  Property price
        property.property.price = 10000
        assertEquals(10000, property.property.price)
        propertyDao.updateProperty(property)
        firstProperty = propertyDao.getAllProperties().first()[0]
        Assert.assertEquals(propertyUpdate.property.price, firstProperty.property.price)

        // Test if update all property is working
        propertyDao.updateProperty(propertyUpdate)
        firstProperty = propertyDao.getAllProperties().first()[0]

        Assert.assertEquals(propertyUpdate.property, firstProperty.property)
        closeDb()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAndSearchPropertyAggregate() = runBlocking {
        //Insert fake properties
        propertyDao.insertProperty(property)

        //First Property in database
        var firstProperty = propertyDao.getAllProperties().first()[0]

        //Fake Property 1 should be equal to first agent in database
        assertEquals(property.property.park, firstProperty.property.park)
        //Fake Property 1 should be equal to first agent in database
        assertEquals(false, firstProperty.property.park)
        closeDb()
    }
}
