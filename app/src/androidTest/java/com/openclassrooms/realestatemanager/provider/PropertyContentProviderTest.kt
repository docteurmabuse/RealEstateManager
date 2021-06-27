package com.openclassrooms.realestatemanager.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.provider.ProviderTestRule
import com.openclassrooms.realestatemanager.data.PropertyFactory
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.provider.PropertyContract.AUTHORITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_URI_PROPERTIES
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@HiltAndroidTest
class PropertyContentProviderTest {
    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var agentDao: AgentDao
    private lateinit var contentResolver: ContentResolver

    //Create fake property
    private val property1 = PropertyFactory.makeProperty()
    private val property2 = PropertyFactory.makeProperty()
    private val property = PropertyFactory.makeOneProperty()
    private val propertyUpdate = PropertyFactory.makeOneUpdateProperty()
    private val PROPERTIES_URI_ALL_ITEMS_CODE = 11

    private val agent =
        AgentEntity("1", "John Wayne", "john45@gmail.com", "121221", "myphotourl.com")


    @get:Rule(order = 0)
    var mProviderRule: ProviderTestRule? =
        ProviderTestRule.Builder(PropertyContentProvider::class.java, AUTHORITY).build()

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()


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
            propertyDao.insertProperty(property1)
            contentResolver =
                InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
        }

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        propertyDatabase.close()
    }

    @Test
    fun verifyContentProviderContractWorks() {
        val resolver = mProviderRule!!.resolver
        val cursor2: Cursor? = resolver.query(
            ContentUris.withAppendedId(
                CONTENT_URI_PROPERTIES,
                PROPERTIES_URI_ALL_ITEMS_CODE.toLong()
            ), null, null, null, null, null
        )
        assertNotNull(cursor2)
        assertEquals(1, cursor2?.count)
    }

    @Test
    @Throws(Exception::class)
    fun getAllPropertiesWithCursor() {
        runBlocking {
            //Insert fake properties
            propertyDao.insertProperty(property1)
            val cursor: Cursor = propertyDao.getAllPropertiesWithCursor()
            val propertiesDbCount = propertyDao.getPropertiesCount()
            assertEquals(propertiesDbCount, cursor.count)
            if (cursor.moveToFirst()) {
                assertEquals(property1.property.id, cursor.getString(cursor.getColumnIndex("id")))
                assertEquals(
                    property1.address.address1,
                    cursor.getString(cursor.getColumnIndex("address1"))
                )
            }
            cursor.close()
        }
    }
}
