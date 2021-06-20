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

    // Content URI for this table. Returns all items.
    val CONTENT_URI_PROPERTIES = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTIES")
    val CONTENT_URI_AGENT = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_AGENT")


    // URI to get the number of entries.
    val ROW_COUNT_URI_AGENT = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_AGENT/$COUNT")
    val ROW_COUNT_URI_PROPERTIES = Uri.parse("content://$AUTHORITY/$CONTENT_PATH_PROPERTIES/$COUNT")


    // Single record mime type
    const val AGENT_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.agent"
    const val PROPERTIES_SINGLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.item/vnd.com.openclassrooms.realestatemanager.provider.properties"


    // Multiple Record MIME type
    const val AGENT_MULTIPLE_RECORDS_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.agent"
    const val PROPERTIES_MULTIPLE_RECORD_MIME_TYPE =
        "vnd.android.cursor.dir/vnd.com.openclassrooms.realestatemanager.provider.properties"

}
