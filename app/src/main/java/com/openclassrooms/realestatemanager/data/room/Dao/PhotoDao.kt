package com.openclassrooms.realestatemanager.data.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.models.Property

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Insert
    suspend fun insertPhotos(photos: List<Photo>): LongArray

    @Query("SELECT * FROM estate_photos WHERE id = :id")
    suspend fun getPropertyById(id: Int): Property

    @Query("DELETE FROM  estate_photos WHERE id IN (:ids)")
    suspend fun deletePhotos(ids: List<Int>): Int

    @Query("DELETE FROM  estate_photos WHERE id = :primaryKey")
    suspend fun deletePhoto(primaryKey: PrimaryKey): Int


}