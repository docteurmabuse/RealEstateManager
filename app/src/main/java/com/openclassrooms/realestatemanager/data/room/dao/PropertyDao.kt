package com.openclassrooms.realestatemanager.data.room.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.models.Property

@Dao
interface PropertyDao {

    @Insert
    suspend fun insertProperty(property: Property): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperties(properties: List<Property>): LongArray

    //Get Property by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Int): Property

    //Get Properties List
    @Query("SELECT * FROM properties ORDER BY sell_date DESC")
    suspend fun getAllProperties(): List<Property>

    //Update Property
    @Update
    suspend fun updateProperty(property: Property)
}