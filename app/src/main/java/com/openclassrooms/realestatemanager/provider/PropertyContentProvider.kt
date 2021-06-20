package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.db.dao.AgentDao
import com.openclassrooms.realestatemanager.db.dao.PropertyDao
import com.openclassrooms.realestatemanager.provider.PropertyContract.AGENT_MULTIPLE_RECORDS_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AGENT_SINGLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AUTHORITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_AGENT
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTIES
import com.openclassrooms.realestatemanager.provider.PropertyContract.COUNT
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_SINGLE_RECORD_MIME_TYPE
import javax.inject.Inject

class PropertyContentProvider @Inject constructor() : ContentProvider() {

    // provide access to the database
    private lateinit var sUriMatcher: UriMatcher
    @Inject
    lateinit var propertyDao: PropertyDao
    @Inject
    lateinit var agentDao: AgentDao

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

        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTIES, PROPERTIES_URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTIES/#", PROPERTIES_URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTIES/$COUNT",
            PROPERTIES_URI_COUNT_CODE
        )
    }

    // The URI Codes
    private val AGENT_URI_ALL_ITEMS_CODE = 10
    private val AGENT_URI_ONE_ITEM_CODE = 20
    private val AGENT_URI_COUNT_CODE = 30
    private val PROPERTIES_URI_ALL_ITEMS_CODE = 11
    private val PROPERTIES_URI_ONE_ITEM_CODE = 21
    private val PROPERTIES_URI_COUNT_CODE = 31

    override fun onCreate(): Boolean {
        initializeUriMatching()
        return true
    }

    override fun getType(uri: Uri): String? = when (sUriMatcher.match(uri)) {
        AGENT_URI_ALL_ITEMS_CODE -> AGENT_MULTIPLE_RECORDS_MIME_TYPE
        AGENT_URI_ONE_ITEM_CODE -> AGENT_SINGLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ALL_ITEMS_CODE -> PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ONE_ITEM_CODE -> PROPERTIES_SINGLE_RECORD_MIME_TYPE
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var cursor: Cursor? = null
        val id = uri.lastPathSegment
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

            UriMatcher.NO_MATCH -> { /*error handling goes here*/
            }
            else -> {
                throw IllegalArgumentException("Query doesn't exist: $uri")
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

}
