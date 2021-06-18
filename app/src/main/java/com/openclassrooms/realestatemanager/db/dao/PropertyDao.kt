package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.property.*
import kotlinx.coroutines.flow.Flow

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
        ORDER BY price ASC
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
        INNER JOIN property_photos ON property_photos.property_id = properties.id
        WHERE ( :textQuery iS NULL OR city LIKE '%' || :textQuery || '%')
        AND (:area IS NULL OR area LIKE '%' || :area || '%')
        AND (:museum IS NULL OR museum IS :museum)
        AND (:school IS NULL OR schools IS :school)
        AND (:shop IS NULL OR shops IS :shop)
        AND (:hospital IS NULL OR hospital IS :hospital)
        AND (:station IS NULL OR stations IS  :station)
        AND (:park IS NULL OR park IS :park)
        AND (:sold IS NULL OR sold IS :sold)
        AND  (type IN (:typeList))
        AND surface BETWEEN :minSurface AND :maxSurface
        AND price BETWEEN :minPrice AND :maxPrice
        AND (:sellDate IS NULL OR sell_date >= :sellDate)
        AND (:soldDate IS NULL OR sold_date >= :soldDate)
        AND (:rooms IS NULL OR room_number >= :rooms)
        AND (:beds IS NULL OR bedroom_number >= :beds)
        AND (:baths IS NULL OR bathroom_number >= :baths)
        ORDER BY :sortBy ASC
   """
    )
    fun filterSearchProperties(
        textQuery: String? = null,
        museum: Int? = null,
        school: Int? = null,
        shop: Int? = null,
        hospital: Int? = null,
        station: Int? = null,
        park: Int? = null,
        area: String? = null,
        typeList: List<String?>?,
        minSurface: Float?,
        maxSurface: Float?,
        minPrice: Float?,
        maxPrice: Float?,
        sold: Int? = null,
        sellDate: Long?,
        soldDate: Long?,
        //  numberOfPics: Int?,
        rooms: Int? = null,
        beds: Int?,
        baths: Int?,
        sortBy: String?
    ): Flow<List<PropertyEntityAggregate>>

    //        AND (:numberOfPics IS NULL OR sold_date < :numberOfPics)

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
