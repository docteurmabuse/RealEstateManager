package com.openclassrooms.realestatemanager.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.models.Photo

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Insert
    suspend fun insertPhotos(photos: List<Photo>): LongArray


    //Get Photos List
    @Query("SELECT * FROM estate_photos ORDER BY name ASC")
    suspend fun getAllPhotos(): List<Photo>

    @Query("DELETE FROM  estate_photos WHERE id IN (:ids)")
    suspend fun deletePhotos(ids: List<Int>): Int

    @Query("DELETE FROM  estate_photos WHERE id = :primaryKey")
    suspend fun deletePhoto(primaryKey: Int): Int

    //Update Photo
    @Update
    suspend fun updatePhoto(photo: Photo)

}