package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.provider.PropertyContract.AUTHORITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_AGENT
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTIES
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTY_ADDRESS
import com.openclassrooms.realestatemanager.provider.PropertyContract.CONTENT_PATH_PROPERTY_PHOTOS
import com.openclassrooms.realestatemanager.provider.PropertyContract.COUNT

class PropertyContentProvider : ContentProvider() {
    // provide access to the database
    private lateinit var sUriMatcher: UriMatcher

    // Add the URI's that can be matched on
    // this content provider
    private fun initializeUriMatching() {

        sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_AGENT, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_AGENT/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_AGENT/$COUNT",
            URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTIES, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTIES/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTIES/$COUNT",
            URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTIES, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTIES/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTIES/$COUNT",
            URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTY_ADDRESS, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTY_ADDRESS/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_ADDRESS/$COUNT",
            URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTY_PHOTOS, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTY_PHOTOS/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_PHOTOS/$COUNT",
            URI_COUNT_CODE
        )
        sUriMatcher.addURI(AUTHORITY, CONTENT_PATH_PROPERTY_PHOTOS, URI_ALL_ITEMS_CODE)
        sUriMatcher.addURI(AUTHORITY, "$CONTENT_PATH_PROPERTY_PHOTOS/#", URI_ONE_ITEM_CODE)
        sUriMatcher.addURI(
            AUTHORITY, "$CONTENT_PATH_PROPERTY_PHOTOS/$COUNT",
            URI_COUNT_CODE
        )
    }

    // The URI Codes
    private val URI_ALL_ITEMS_CODE = 10
    private val URI_ONE_ITEM_CODE = 20
    private val URI_COUNT_CODE = 30

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        TODO("Implement this to initialize your content provider on startup.")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        TODO("Implement this to handle query requests from clients.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
