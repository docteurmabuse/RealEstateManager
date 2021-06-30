package com.openclassrooms.realestatemanager.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.provider.ProviderTestRule
import com.openclassrooms.realestatemanager.data.PropertyFactory
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.provider.PropertyContract.AUTHORITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ADDRESS1
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ADDRESS2
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_AREA
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_CITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_COUNTRY
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_LAT
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_LNG
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_PROPERTY_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_STATE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ZIPCODE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_EMAIL
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_NAME
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_PHONE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_PHOTO_URL
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_URI_AGENT
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_URI_PROPERTIES
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_NAME
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_PHOTO_PATH
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_PROPERTY_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_AGENT_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_BATHROOM_NUMBER
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_BEDROOM_NUMBER
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_DESCRIPTION
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_HOSPITAL
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_MUSEUM
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_PARK
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_PRICE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_ROOM_NUMBER
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SCHOOLS
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SELL_DATE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SHOPS
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SOLD
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SOLD_DATE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_STATIONS
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_SURFACE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PropertiesTable.Columns.KEY_PROPERTY_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_NAME
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_PROPERTY_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_VIDEO_PATH
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


    private val agent =
        AgentEntity("1", "John Wayne", "john45@gmail.com", "121221", "myphotourl.com")

    private val testAgentUrl = "${CONTENT_URI_AGENT}/${agent.id}".toUri()
    private val testPropertyUrl = "${CONTENT_URI_PROPERTIES}/${property1.property.id}".toUri()

    private var agentUri: Uri? = null
    private var propertyUri: Uri? = null
    private val testAgentContentValues = generateAgentItem()
    private var testPropertyContentValues = generatePropertyItem()

    @get:Rule
    var mProviderRule: ProviderTestRule? =
        ProviderTestRule.Builder(
            PropertyContentProvider::class.java,
            AUTHORITY
        ).build()

    @Before
    fun init() {
        runBlocking {
            //init resolver
            contentResolver =
                mProviderRule!!.resolver
        }
    }

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
    fun verifyContentProvideContractWorks() {
        // perform some database (or other) operations
        agentUri = contentResolver.insert(testAgentUrl, testAgentContentValues)
        // perform some assertions on the resulting URI
        propertyUri = contentResolver.insert(testAgentUrl, testPropertyContentValues)
        assertNotNull(propertyUri)
    }


    @Test
    @Throws(Exception::class)
    fun getAllPropertiesWithCursor() {
        runBlocking {
            //Insert fake properties
            propertyDao.insertProperty(property1)
            val cursor: Cursor = propertyDao.getAllPropertiesWithCursor()
            val propertiesDbCount = propertyDao.getPropertiesCount()
            assertEquals(1, cursor.count)
            assertEquals(propertiesDbCount, cursor.count)
            if (cursor.moveToFirst()) {
                assertEquals(
                    property1.property.id, cursor.getString(
                        cursor.getColumnIndex(
                            KEY_PROPERTY_ID
                        )
                    )
                )
                assertEquals(
                    property1.address.address1,
                    cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_ADDRESS1))
                )
            }
            cursor.close()
        }
    }

    @Test
    fun getCursorAgentCountIsSameGetDatabaseCount() {
        runBlocking {
            val resolver = mProviderRule!!.resolver
            val cursor: Cursor? = resolver.query(CONTENT_URI_AGENT, null, null, null, null)
            assertNotNull(cursor)
            cursor?.close()
        }
    }

    @Test
    fun getCursorPropertyCountIsSameGetDatabaseCount() {
        runBlocking {
            val resolver = mProviderRule!!.resolver
            val cursor: Cursor? = resolver.query(
                CONTENT_URI_PROPERTIES, null, null, null, null
            )
            assertNotNull(cursor)
            cursor?.close()
        }
    }

    private fun generatePropertyItem(): ContentValues? {
        val values = ContentValues()
        values.put(KEY_PROPERTY_ID, property1.property.id)
        values.put(KEY_PROPERTY_TYPE, property1.property.type)
        values.put(KEY_PROPERTY_PRICE, property1.property.price)
        values.put(KEY_PROPERTY_SURFACE, property1.property.surface)
        values.put(KEY_PROPERTY_ROOM_NUMBER, property1.property.roomNumber)
        values.put(KEY_PROPERTY_BATHROOM_NUMBER, property1.property.bathroomNumber)
        values.put(KEY_PROPERTY_BEDROOM_NUMBER, property1.property.bedroomNumber)
        values.put(KEY_PROPERTY_DESCRIPTION, property1.property.description)
        values.put(KEY_PROPERTY_SCHOOLS, property1.property.schools)
        values.put(KEY_PROPERTY_SHOPS, property1.property.shops)
        values.put(KEY_PROPERTY_PARK, property1.property.park)
        values.put(KEY_PROPERTY_STATIONS, property1.property.stations)
        values.put(KEY_PROPERTY_HOSPITAL, property1.property.hospital)
        values.put(KEY_PROPERTY_MUSEUM, property1.property.museum)
        values.put(KEY_PROPERTY_SOLD, property1.property.sold)
        values.put(KEY_PROPERTY_SELL_DATE, property1.property.sellDate)
        values.put(KEY_PROPERTY_SOLD_DATE, property1.property.soldDate)
        values.put(KEY_PROPERTY_AGENT_ID, property1.property.agent_id)

        values.put(KEY_ADDRESS_ID, property1.address.address_id)
        values.put(KEY_ADDRESS_PROPERTY_ID, property1.address.address_id)
        values.put(KEY_ADDRESS_ADDRESS1, property1.address.address1)
        values.put(KEY_ADDRESS_ADDRESS2, property1.address.address2)
        values.put(KEY_ADDRESS_CITY, property1.address.city)
        values.put(KEY_ADDRESS_ZIPCODE, property1.address.zipCode)
        values.put(KEY_ADDRESS_STATE, property1.address.state)
        values.put(KEY_ADDRESS_COUNTRY, property1.address.country)
        values.put(KEY_ADDRESS_AREA, property1.address.area)
        values.put(KEY_ADDRESS_LAT, property1.address.lat)
        values.put(KEY_ADDRESS_LNG, property1.address.lng)

        values.put(KEY_PHOTO_ID, property1.photos[0].photo_id)
        values.put(KEY_PHOTO_PROPERTY_ID, property1.photos[0].property_id)
        values.put(KEY_PHOTO_NAME, property1.photos[0].name)
        values.put(KEY_PHOTO_PHOTO_PATH, property1.photos[0].photoPath)

        values.put(KEY_VIDEO_ID, property1.videos[0].video_id)
        values.put(KEY_VIDEO_PROPERTY_ID, property1.videos[0].property_id)
        values.put(KEY_VIDEO_NAME, property1.videos[0].name)
        values.put(KEY_VIDEO_VIDEO_PATH, property1.videos[0].videoPath)

        return values
    }

    private fun generateAgentItem(): ContentValues? {
        val values = ContentValues()
        values.put(KEY_AGENT_ID, agent.id)
        values.put(KEY_AGENT_NAME, agent.id)
        values.put(KEY_AGENT_EMAIL, agent.id)
        values.put(KEY_AGENT_PHONE, agent.id)
        values.put(KEY_AGENT_PHOTO_URL, agent.id)
        return values
    }
}
