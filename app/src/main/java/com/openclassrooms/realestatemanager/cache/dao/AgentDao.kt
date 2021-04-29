package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AgentDao {

    //Insert AgentCacheEntity
    @Insert
    suspend fun insertAgent(agent: AgentCacheEntity): Long

    //Get AgentCacheEntity by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    suspend fun getAgentById(id: Int): AgentCacheEntity

    //Update AgentCacheEntity
    @Update
    suspend fun updateAgent(agent: AgentCacheEntity)
}