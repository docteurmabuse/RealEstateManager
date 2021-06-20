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
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTY_ADDRESS
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTY_PHOTOS
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTY_VIDEOS
import com.openclassrooms.realestatemanager.provider.PropertyContract.COUNT
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTIES_SINGLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_ADDRESS_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_ADDRESS_SINGLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_PHOTOS_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_PHOTOS_SINGLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_VIDEOS_MULTIPLE_RECORD_MIME_TYPE
import com.openclassrooms.realestatemanager.provider.PropertyContract.PROPERTY_VIDEOS_SINGLE_RECORD_MIME_TYPE
import javax.inject.Inject

class PropertyContentProvider @Inject constructor(
    private val propertyDao: PropertyDao,
    private val agentDao: AgentDao
) : ContentProvider() {
    // provide access to the database
    private lateinit var sUriMatcher: UriMatcher

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

        sUriMatcher.addURI(
            AUTHORITY,
            CONTENT_PATH_PROPERTY_ADDRESS,
            PROPERTY_ADDRESS_URI_ALL_ITEMS_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY,
            "$CONTENT_PATH_PROPERTY_ADDRESS/#",
            PROPERTY_ADDRESS_URI_ONE_ITEM_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_ADDRESS/$COUNT",
            PROPERTY_ADDRESS_URI_COUNT_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY,
            CONTENT_PATH_PROPERTY_PHOTOS,
            PROPERTY_PHOTOS_URI_ALL_ITEMS_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY,
            "$CONTENT_PATH_PROPERTY_PHOTOS/#",
            PROPERTY_PHOTOS_URI_ONE_ITEM_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_PHOTOS/$COUNT",
            PROPERTY_PHOTOS_URI_COUNT_CODE
        )

        sUriMatcher.addURI(
            AUTHORITY,
            CONTENT_PATH_PROPERTY_VIDEOS,
            PROPERTY_VIDEOS_URI_ALL_ITEMS_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY,
            "$CONTENT_PATH_PROPERTY_VIDEOS/#",
            PROPERTY_VIDEOS_URI_ONE_ITEM_CODE
        )
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_VIDEOS/$COUNT",
            PROPERTY_VIDEOS_URI_COUNT_CODE
        )
    }

    // The URI Codes
    private val AGENT_URI_ALL_ITEMS_CODE = 10
    private val AGENT_URI_ONE_ITEM_CODE = 20
    private val AGENT_URI_COUNT_CODE = 30
    private val PROPERTIES_URI_ALL_ITEMS_CODE = 11
    private val PROPERTIES_URI_ONE_ITEM_CODE = 21
    private val PROPERTIES_URI_COUNT_CODE = 31
    private val PROPERTY_ADDRESS_URI_ALL_ITEMS_CODE = 12
    private val PROPERTY_ADDRESS_URI_ONE_ITEM_CODE = 22
    private val PROPERTY_ADDRESS_URI_COUNT_CODE = 32
    private val PROPERTY_PHOTOS_URI_ALL_ITEMS_CODE = 13
    private val PROPERTY_PHOTOS_URI_ONE_ITEM_CODE = 23
    private val PROPERTY_PHOTOS_URI_COUNT_CODE = 33
    private val PROPERTY_VIDEOS_URI_ALL_ITEMS_CODE = 14
    private val PROPERTY_VIDEOS_URI_ONE_ITEM_CODE = 24
    private val PROPERTY_VIDEOS_URI_COUNT_CODE = 34

    override fun onCreate(): Boolean {
        initializeUriMatching()
        return true
    }

    override fun getType(uri: Uri): String? = when (sUriMatcher.match(uri)) {
        AGENT_URI_ALL_ITEMS_CODE -> AGENT_MULTIPLE_RECORDS_MIME_TYPE
        AGENT_URI_ONE_ITEM_CODE -> AGENT_SINGLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ALL_ITEMS_CODE -> PROPERTIES_MULTIPLE_RECORD_MIME_TYPE
        PROPERTIES_URI_ONE_ITEM_CODE -> PROPERTIES_SINGLE_RECORD_MIME_TYPE
        PROPERTY_ADDRESS_URI_ALL_ITEMS_CODE -> PROPERTY_ADDRESS_MULTIPLE_RECORD_MIME_TYPE
        PROPERTY_ADDRESS_URI_ONE_ITEM_CODE -> PROPERTY_ADDRESS_SINGLE_RECORD_MIME_TYPE
        PROPERTY_PHOTOS_URI_ALL_ITEMS_CODE -> PROPERTY_PHOTOS_MULTIPLE_RECORD_MIME_TYPE
        PROPERTY_PHOTOS_URI_ONE_ITEM_CODE -> PROPERTY_PHOTOS_SINGLE_RECORD_MIME_TYPE
        PROPERTY_VIDEOS_URI_ALL_ITEMS_CODE -> PROPERTY_VIDEOS_MULTIPLE_RECORD_MIME_TYPE
        PROPERTY_VIDEOS_URI_ONE_ITEM_CODE -> PROPERTY_VIDEOS_SINGLE_RECORD_MIME_TYPE
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
                cursor = propertyDao.getAllPropertiesWithCursor()
            }
            AGENT_URI_ONE_ITEM_CODE -> {
                cursor = id?.let { propertyDao.getPropertyByIdWithCursor(it) }
            }
            AGENT_URI_COUNT_CODE -> {
                cursor = propertyDao.getPropertiesCountWithCursor()
            }

            UriMatcher.NO_MATCH -> { /*error handling goes here*/
            }
            else -> { /*unexpected problem*/
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
