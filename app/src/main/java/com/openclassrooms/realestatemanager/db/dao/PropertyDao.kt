package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.property.*
import kotlinx.coroutines.flow.Flow
import javax.annotation.Nullable

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyAggregate(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity
    )

    suspend fun insertProperty(
        propertyEntityAggregate: PropertyEntityAggregate
    ) {
        insertPropertyAggregate(
            propertyEntityAggregate.property,
            propertyEntityAggregate.photos,
            propertyEntityAggregate.videos,
            propertyEntityAggregate.address
        )
    }

    @Transaction
    //Get PropertyEntity by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    fun getPropertyById(id: String): Flow<PropertyEntityAggregate>

    @Transaction
    //Get PropertiesEntity List
    @Query(
        "SELECT * from  PROPERTIES ORDER BY sell_date ASC"
    )
    fun getAllProperties(): Flow<List<PropertyEntityAggregate>>

    // Retrieve PropertiesEntity from search word
    @Transaction
    @Query(
        """
        SELECT *
        FROM PROPERTIES
        INNER JOIN property_address ON property_address.property_id = properties.id
        WHERE type LIKE '%' || :searchQuery || '%'  
        OR address1  LIKE '%' || :searchQuery || '%'  
        OR area  LIKE '%' || :searchQuery || '%'  
        OR state  LIKE '%' || :searchQuery || '%'  
        OR city  LIKE '%' || :searchQuery || '%'
        ORDER BY sell_date ASC
   """
    )
    fun searchProperties(
        searchQuery: String,
    ): Flow<List<PropertyEntityAggregate>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM PROPERTIES
        INNER JOIN property_address ON property_address.property_id = properties.id
        WHERE type LIKE '%' || :textQuery || '%'  
        OR address1  LIKE '%' || :textQuery || '%'  
        OR area  LIKE '%' || :textQuery || '%'  
        OR state  LIKE '%' || :textQuery || '%'  
        OR city  LIKE '%' || :textQuery || '%'
        AND area = :area
        AND museum =:museum
        AND schools =:school
        AND shops =:shop
        AND hospital=:hospital
        AND stations=:station
        AND park=:park
        AND area =:area
        and type IN (:types)
        ORDER BY sell_date ASC
   """
    )
    fun filterSearchProperties(
        @Nullable
        textQuery: String?,
        @Nullable
        museum: Int?,
        @Nullable
        school: Int?,
        @Nullable
        shop: Int?,
        @Nullable
        hospital: Int?,
        @Nullable
        station: Int?,
        @Nullable
        park: Int?,
        @Nullable
        area: String?,
        @Nullable
        types: List<String>?
    ): Flow<List<PropertyEntityAggregate>>

    //Update PropertyEntity
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePropertyAggregate(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity
    )

    suspend fun updateProperty(
        propertyEntityAggregate: PropertyEntityAggregate
    ) {
        updatePropertyAggregate(
            propertyEntityAggregate.property,
            propertyEntityAggregate.photos,
            propertyEntityAggregate.videos,
            propertyEntityAggregate.address
        )
    }
}
