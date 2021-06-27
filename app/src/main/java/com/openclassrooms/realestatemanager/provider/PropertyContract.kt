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

    // Table Constants
    object PropertiesTable {
        // The table name
        const val TABLE_NAME: String = "properties"

        // The constants for the table columns
        object Columns {
            const val KEY_PROPERTY_ID: String = "id"
            const val KEY_PROPERTY_TYPE: String = "type"
            const val KEY_PROPERTY_PRICE: String = "price"
            const val KEY_PROPERTY_SURFACE: String = "surface"
            const val KEY_PROPERTY_ROOM_NUMBER: String = "roomNumber"
            const val KEY_PROPERTY_BATHROOM_NUMBER: String = "bathroomNumber"
            const val KEY_PROPERTY_BEDROOM_NUMBER: String = "bedroomNumber"
            const val KEY_PROPERTY_DESCRIPTION: String = "description"
            const val KEY_PROPERTY_SCHOOLS: String = "schools"
            const val KEY_PROPERTY_SHOPS: String = "shops"
            const val KEY_PROPERTY_PARK: String = "shops"
            const val KEY_PROPERTY_STATIONS: String = "stations"
            const val KEY_PROPERTY_HOSPITAL: String = "hospital"
            const val KEY_PROPERTY_MUSEUM: String = "museum"
            const val KEY_PROPERTY_SOLD: String = "sold"
            const val KEY_PROPERTY_SELL_DATE: String = "sell_date"
            const val KEY_PROPERTY_SOLD_DATE: String = "sold_date"
            const val KEY_PROPERTY_MEDIA: String = "media"
            const val KEY_PROPERTY_AGENT: String = "agent"
            const val KEY_PROPERTY_ADDRESS: String = "address"
        }

    }
}
