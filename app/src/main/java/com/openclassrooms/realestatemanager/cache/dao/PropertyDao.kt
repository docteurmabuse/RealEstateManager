package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.cache.model.PropertyEntity

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: PropertyEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperties(properties: List<PropertyEntity>): LongArray

    //Get PropertyEntity by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Int): PropertyEntity

    //Get PropertiesEntity List
    @Query("SELECT * FROM properties ORDER BY sell_date DESC")
    suspend fun getAllProperties(): List<PropertyEntity>

    // Retrieve PropertiesEntity List form a query
    @Query("SELECT * FROM properties WHERE surface BETWEEN 200 AND 300 OR price  BETWEEN 1500000 AND 2000000 OR area LIKE  '%' || :query || '%' ORDER BY sell_date DESC")
    suspend fun searchProperties(
        query: String
    ): List<PropertyEntity>

    //Update PropertyEntity
    @Update
    suspend fun updateProperty(property: PropertyEntity)
}