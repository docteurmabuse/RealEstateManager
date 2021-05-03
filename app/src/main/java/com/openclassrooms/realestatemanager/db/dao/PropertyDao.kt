package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPropertyAggregate(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>
    ): Long

    suspend fun insertProperty(propertyAggregate: PropertyEntityAggregate): Long {
        return insertPropertyAggregate(
            propertyAggregate.property,
            propertyAggregate.photos,
            propertyAggregate.videos
        )
    }

    //Get PropertyEntity by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Int): PropertyEntityAggregate

    //Get PropertiesEntity List
    @Query("SELECT * FROM properties ORDER BY sell_date DESC")
    suspend fun getAllProperties(): Flow<List<PropertyEntityAggregate>>

    // Retrieve PropertiesEntity List form a query
    @Query("SELECT * FROM properties WHERE surface BETWEEN 200 AND 300 OR price  BETWEEN 1500000 AND 2000000 OR area LIKE  '%' || :query || '%' ORDER BY sell_date DESC")
    suspend fun searchProperties(
        query: String
    ): Flow<List<PropertyEntityAggregate>>

    //Update PropertyEntity
    @Update
    suspend fun updateProperty(property: PropertyEntityAggregate)
}