package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(photo: PhotoCacheEntity): Long

    @Insert
    suspend fun insertPhotos(photos: List<PhotoCacheEntity>): LongArray


    //Get Photos List
    @Query("SELECT * FROM estate_photos ORDER BY name ASC")
    suspend fun getAllPhotos(): List<PhotoCacheEntity>

    @Query("DELETE FROM  estate_photos WHERE id IN (:ids)")
    suspend fun deletePhotos(ids: List<Int>): Int

    @Query("DELETE FROM  estate_photos WHERE id = :primaryKey")
    suspend fun deletePhoto(primaryKey: Int): Int

    //Update PhotoCacheEntity
    @Update
    suspend fun updatePhoto(photo: PhotoCacheEntity)

}