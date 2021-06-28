package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.db.database.PropertyDatabase
import com.openclassrooms.realestatemanager.provider.PropertyContract.AGENT_MULTIPLE_RECORDS_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AGENT_SINGLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AUTHORITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_AGENT
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTIES
import com.openclassrooms.realestatemanager.provider.PropertyContract.COUNT
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_SINGLE_RECORD_MIME_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class PropertyContentProvider : ContentProvider(), CoroutineScope {

    // provide access to the database
    private lateinit var sUriMatcher: UriMatcher

    private lateinit var propertyDatabase: PropertyDatabase
    private lateinit var propertyDao: PropertyDao

    private lateinit var agentDao: AgentDao


    override fun onCreate(): Boolean {
        propertyDatabase = getRoomDatabase()
        propertyDao = propertyDatabase.propertyDao()
        agentDao = propertyDatabase.agentDao()
        initializeUriMatching()
        return true
    }

    private fun getRoomDatabase(): PropertyDatabase =
        PropertyDatabase.getDatabase(context!!)


    // Add the URI's that can be matched on
    // this content provider
    private fun initializeUriMatching() {
        sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_AGENT, AGENT_URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_AGENT/#", AGENT_URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_AGENT/$COUNT",
            AGENT_URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_AGENT/*", AGENT_URI_ONE_ITEM_CODE)

        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTIES, PROPERTIES_URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTIES/#", PROPERTIES_URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTIES/$COUNT",
            PROPERTIES_URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTIES/*", PROPERTIES_URI_ONE_ITEM_CODE)
    }

    // The URI Codes
    private val AGENT_URI_ALL_ITEMS_CODE = 10
    private val AGENT_URI_ONE_ITEM_CODE = 20
    private val AGENT_URI_COUNT_CODE = 30
    private val PROPERTIES_URI_ALL_ITEMS_CODE = 11
    private val PROPERTIES_URI_ONE_ITEM_CODE = 21
    private val PROPERTIES_URI_COUNT_CODE = 31


    override fun getType(uri: Uri): String? = when (sUriMatcher.match(uri)) {
        AGENT_URI_ALL_ITEMS_CODE -> AGENT_MULTIPLE_RECORDS_MIME_TYPE
        AGENT_URI_ONE_ITEM_CODE -> AGENT_SINGLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ALL_ITEMS_CODE -> PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ONE_ITEM_CODE -> PROPERTIES_SINGLE_RECORD_MIME_TYPE
        AGENT_URI_COUNT_CODE -> PROPERTIES_SINGLE_RECORD_MIME_TYPE
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var cursor: Cursor? = null
        val id = uri.lastPathSegment
        runBlocking {
            when (sUriMatcher.match(uri)) {
                PROPERTIES_URI_ALL_ITEMS_CODE -> {
                    cursor = propertyDao.getAllPropertiesWithCursor()
                }

                PROPERTIES_URI_ONE_ITEM_CODE -> {
                    cursor = id?.let { propertyDao.getPropertyByIdWithCursor(it) }
                }

                PROPERTIES_URI_COUNT_CODE -> {
                    cursor = propertyDao.getPropertiesCountWithCursor()
                }

                AGENT_URI_ALL_ITEMS_CODE -> {
                    cursor = agentDao.getAllAgentsWithCursor()
                }

                AGENT_URI_ONE_ITEM_CODE -> {
                    cursor = id?.let { agentDao.getAgentByIdWithCursor(it) }
                }

                AGENT_URI_COUNT_CODE -> {
                    cursor = agentDao.getAgentCountWithCursor()
                }

                UriMatcher.NO_MATCH -> {
                    throw IllegalArgumentException("Query is not matching: $uri")
                }

                else -> {
                    throw IllegalArgumentException("Query doesn't exist: $uri")
                }
            }
        }
        return cursor
    }

    // Insert a record for test purpose
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null && values != null) {
            when (sUriMatcher.match(uri)) {
                AGENT_URI_ONE_ITEM_CODE -> {
                    val agent = agentFromContentValues(values)
                    runBlocking {
                        agentDao.insertAgent(agent)
                        context!!.contentResolver.notifyChange(uri, null)
                    }
                    return "$uri/${agent.id}".toUri()
                }
                PROPERTIES_URI_ONE_ITEM_CODE -> {
                    val property = propertyFromContentValues(values)
                    val address = addressFromContentValues(values)
                    val photos = photosFromContentValues(values)
                    val videos = videosFromContentValues(values)

                    runBlocking {
                        context!!.contentResolver.notifyChange(uri, null)
                        propertyDao.insertPropertyAggregate(
                            property,
                            photos,
                            videos,
                            address,
                        )
                    }
                    return "$uri/${property.id}".toUri()
                }
                else -> {
                    throw IllegalArgumentException("Query doesn't exist: $uri")
                }
            }
        }
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
