package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.cache.model.PhotoEntity

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(photo: PhotoEntity): Long

    @Insert
    suspend fun insertPhotos(photos: List<PhotoEntity>): LongArray


    //Get Photos List
    @Query("SELECT * FROM estate_photos ORDER BY name ASC")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Query("DELETE FROM  estate_photos WHERE id IN (:ids)")
    suspend fun deletePhotos(ids: List<Int>): Int

    @Query("DELETE FROM  estate_photos WHERE id = :primaryKey")
    suspend fun deletePhoto(primaryKey: Int): Int

    //Update PhotoEntity
    @Update
    suspend fun updatePhoto(photo: PhotoEntity)

}