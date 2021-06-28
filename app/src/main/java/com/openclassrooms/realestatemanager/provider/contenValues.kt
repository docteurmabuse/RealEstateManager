package com.openclassrooms.realestatemanager.provider

import android.content.ContentValues
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.AddressEntity
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ADDRESS1
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ADDRESS2
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_AREA
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_CITY
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_COUNTRY
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_LAT
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_LNG
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_STATE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AddressTable.Columns.KEY_ADDRESS_ZIPCODE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_EMAIL
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_NAME
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_PHONE
import com.openclassrooms.realestatemanager.provider.PropertyContract.AgentTable.Columns.KEY_AGENT_PHOTO_URL
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_ID
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_NAME
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_PHOTO_PATH
import com.openclassrooms.realestatemanager.provider.PropertyContract.PhotosTable.Columns.KEY_PHOTO_PROPERTY_ID
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
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_PHOTO_PATH
import com.openclassrooms.realestatemanager.provider.PropertyContract.VideosTable.Columns.KEY_VIDEO_PROPERTY_ID

fun propertyFromContentValues(values: ContentValues): PropertyEntity {
    /*  if(values.containsKey(KEY_PROPERTY_ID)) property.id = values.getAsString(KEY_PROPERTY_ID)
    if(values.containsKey(KEY_PROPERTY_TYPE)) property.type =values.getAsString(values.getAsString(KEY_PROPERTY_TYPE))
    if(values.containsKey(KEY_PROPERTY_PRICE)) property.price = values.getAsInteger(KEY_PROPERTY_PRICE)
    if(values.containsKey(KEY_PROPERTY_SURFACE)) property.surface = values.getAsInteger(KEY_PROPERTY_SURFACE)
    if(values.containsKey(KEY_PROPERTY_ROOM_NUMBER)) property.roomNumber = values.getAsInteger(KEY_PROPERTY_ROOM_NUMBER)
    if(values.containsKey(KEY_PROPERTY_BATHROOM_NUMBER)) property.bedroomNumber= values.getAsInteger(KEY_PROPERTY_BATHROOM_NUMBER)
    if(values.containsKey(KEY_PROPERTY_BEDROOM_NUMBER)) property.bathroomNumber = values.getAsInteger(KEY_PROPERTY_BEDROOM_NUMBER)
    if(values.containsKey(KEY_PROPERTY_DESCRIPTION)) property.description = values.getAsString(KEY_PROPERTY_DESCRIPTION)
    if(values.containsKey(KEY_PROPERTY_SCHOOLS)) property.schools = values.getAsBoolean(
        KEY_PROPERTY_SCHOOLS)
    if(values.containsKey(KEY_PROPERTY_SHOPS)) property.shops = values.getAsBoolean(
        KEY_PROPERTY_SHOPS)
    if(values.containsKey(KEY_PROPERTY_PARK)) property.park = values.getAsBoolean(
        KEY_PROPERTY_PARK)
    if(values.containsKey(KEY_PROPERTY_STATIONS)) property.stations = values.getAsBoolean(
        KEY_PROPERTY_STATIONS)
    if(values.containsKey(KEY_PROPERTY_HOSPITAL)) property.hospital = values.getAsBoolean(
        KEY_PROPERTY_HOSPITAL)
    if(values.containsKey(KEY_PROPERTY_MUSEUM)) property.museum = values.getAsBoolean(
        KEY_PROPERTY_MUSEUM)
    if(values.containsKey(KEY_PROPERTY_SOLD)) property.sold = values.getAsBoolean(
        KEY_PROPERTY_SOLD)
    if(values.containsKey(KEY_PROPERTY_SELL_DATE)) property.sellDate = values.getAsLong(
        KEY_PROPERTY_SELL_DATE)
    if(values.containsKey(KEY_PROPERTY_SOLD_DATE)) property.soldDate = values.getAsLong(
        KEY_PROPERTY_SOLD_DATE)
    if(values.containsKey(KEY_PROPERTY_AGENT_ID)) property.agent_id = values.getAsString(KEY_PROPERTY_ID)*/

    return PropertyEntity(
        id = values.getAsString(KEY_PROPERTY_ID),
        type = values.getAsString(values.getAsString(KEY_PROPERTY_TYPE)),
        price = values.getAsInteger(KEY_PROPERTY_PRICE),
        surface = values.getAsInteger(KEY_PROPERTY_SURFACE),
        roomNumber = values.getAsInteger(KEY_PROPERTY_ROOM_NUMBER),
        bathroomNumber = values.getAsInteger(KEY_PROPERTY_BATHROOM_NUMBER),
        bedroomNumber = values.getAsInteger(KEY_PROPERTY_BEDROOM_NUMBER),
        description = values.getAsString(KEY_PROPERTY_DESCRIPTION),
        schools = values.getAsBoolean(
            KEY_PROPERTY_SCHOOLS
        ),
        shops = values.getAsBoolean(
            KEY_PROPERTY_SHOPS
        ),
        park = values.getAsBoolean(
            KEY_PROPERTY_PARK
        ),
        stations = values.getAsBoolean(
            KEY_PROPERTY_STATIONS
        ),
        hospital = values.getAsBoolean(
            KEY_PROPERTY_HOSPITAL
        ),
        museum = values.getAsBoolean(
            KEY_PROPERTY_MUSEUM
        ),
        sold = values.getAsBoolean(
            KEY_PROPERTY_SOLD
        ),
        sellDate = values.getAsLong(
            KEY_PROPERTY_SELL_DATE
        ),
        soldDate = values.getAsLong(
            KEY_PROPERTY_SOLD_DATE
        ),
        agent_id = values.getAsString(KEY_PROPERTY_ID)
    )
}

fun addressFromContentValues(values: ContentValues): AddressEntity {
    return AddressEntity(
        address_id = values.getAsLong(KEY_ADDRESS_ID),
        property_id = values.getAsString(KEY_PROPERTY_ID),
        address1 = values.getAsString(KEY_ADDRESS_ADDRESS1),
        address2 = values.getAsString(KEY_ADDRESS_ADDRESS2),
        city = values.getAsString(KEY_ADDRESS_CITY),
        zipCode = values.getAsString(KEY_ADDRESS_ZIPCODE),
        state = values.getAsString(KEY_ADDRESS_STATE),
        country = values.getAsString(KEY_ADDRESS_COUNTRY),
        area = values.getAsString(KEY_ADDRESS_AREA),
        lat = values.getAsDouble(KEY_ADDRESS_LAT),
        lng = values.getAsDouble(KEY_ADDRESS_LNG)
    )
}

fun photosFromContentValues(values: ContentValues): List<PhotoEntity> {
    PhotoEntity(
        photo_id = values.getAsLong(KEY_PHOTO_ID),
        property_id = values.getAsString(KEY_PHOTO_PROPERTY_ID),
        name = values.getAsString(KEY_PHOTO_NAME),
        photoPath = values.getAsString(KEY_PHOTO_PHOTO_PATH),
    )
    val photos = mutableListOf<PhotoEntity>()

    return photos
}

fun videosFromContentValues(values: ContentValues): List<VideoEntity> {
    VideoEntity(
        video_id = values.getAsLong(KEY_VIDEO_ID),
        property_id = values.getAsString(KEY_VIDEO_PROPERTY_ID),
        name = values.getAsString(KEY_VIDEO_NAME),
        videoPath = values.getAsString(KEY_VIDEO_PHOTO_PATH),
    )
    val videos = mutableListOf<VideoEntity>()

    return videos
}

fun agentFromContentValues(values: ContentValues): AgentEntity {
    return AgentEntity(
        id = values.getAsString(KEY_AGENT_ID),
        name = values.getAsString(KEY_AGENT_NAME),
        email = values.getAsString(KEY_AGENT_EMAIL),
        phone = values.getAsString(KEY_AGENT_PHONE),
        photoUrl = values.getAsString(KEY_AGENT_PHOTO_URL),
    )
}



