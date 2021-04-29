package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.*

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperty(property: PropertyCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperties(properties: List<PropertyCacheEntity>): LongArray

    //Get PropertyCacheEntity by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Int): PropertyCacheEntity

    //Get Properties List
    @Query("SELECT * FROM properties ORDER BY sell_date DESC")
    suspend fun getAllProperties(): List<PropertyCacheEntity>

    //Update PropertyCacheEntity
    @Update
    suspend fun updateProperty(property: PropertyCacheEntity)
}