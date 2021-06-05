package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.property.*
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

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
    @Query("SELECT * FROM properties WHERE type LIKE '%' || :searchQuery || '%'   ORDER BY sell_date ASC")
    fun getAllProperties(searchQuery: String): Flow<List<PropertyEntityAggregate>>



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
        Timber.tag("UPDATE_FabClick")
            .d("UPDATE_FabClick2: ${propertyEntityAggregate.photos}s,\n")

    }

    // Retrieve PropertiesEntity List form a query
    //@Query("SELECT * FROM properties WHERE surface BETWEEN 200 AND 300 OR price  BETWEEN 1500000 AND 2000000  || :query || '%' ORDER BY sell_date DESC")
    //   suspend fun searchProperties(
    //   query: String
//    ): List<PropertyEntityAggregate>
}
