package com.openclassrooms.realestatemanager.provider

import android.net.Uri

object PropertyContract {
    // The URI Code for All items
    const val ALL_ITEMS = -2

    //The URI suffix for counting records
    const val COUNT = "count"

    //URI Authority
    const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"

    //  public tables.
    const val CONTENT_PATH_AGENT = "agent"
    const val CONTENT_PATH_PROPERTIES = "properties"
    const val CONTENT_PATH_PROPERTY_ADDRESS = "property_address"
    const val CONTENT_PATH_PROPERTY_PHOTOS = "property_photos"
    const val CONTENT_PATH_PROPERTY_VIDEOS = "property_videos"

    // Content URI for this table. Returns all items.
    val CONTENT_URI_PROPERTIES = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTIES")
    val CONTENT_URI_AGENT = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_AGENT")
    val CONTENT_URI_PROPERTY_ADDRESS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_ADDRESS")
    val CONTENT_URI_PROPERTY_PHOTOS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_PHOTOS")
    val CONTENT_URI_PROPERTY_VIDEOS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_VIDEOS")

    // URI to get the number of entries.
    val ROW_COUNT_URI_AGENT = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_AGENT/$COUNT")
    val ROW_COUNT_URI_PROPERTIES = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTIES/$COUNT")
    val ROW_COUNT_URI_PROPERTY_ADDRESS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_ADDRESS/$COUNT")
    val ROW_COUNT_URI_PROPERTY_PHOTOS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_PHOTOS/$COUNT")
    val ROW_COUNT_URI_PROPERTY_VIDEOS =
        Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTY_VIDEOS/$COUNT")

    // Single record mime type
    const val AGENT_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.agent"
    const val PROPERTIES_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.properties"
    const val PROPERTY_ADDRESS_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.property_address"
    const val PROPERTY_PHOTOS_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.property_photos"
    const val PROPERTY_VIDEOS_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.property_videos"


    // Multiple Record MIME type
    const val AGENT_MULTIPLE_RECORDS_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.agent"
    const val PROPERTIES_MULTIPLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.properties"
    const val PROPERTY_ADDRESS_MULTIPLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.property_address"
    const val PROPERTY_PHOTOS_MULTIPLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.property_photos"
    const val PROPERTY_VIDEOS_MULTIPLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.property_videos"

    // Database name
    const val DATABASE_NAME: String = "property_db"

    // Table Constants
    object PropertiesTable {
        // The table name
        const val TABLE_NAME: String = "properties"

        // The constants for the table columns
        object Columns
    }
}
