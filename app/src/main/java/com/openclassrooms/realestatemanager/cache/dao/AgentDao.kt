package com.openclassrooms.realestatemanager.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.cache.model.AgentEntity

@Dao
interface AgentDao {

    //Insert AgentEntity
    @Insert
    suspend fun insertAgent(agent: AgentEntity): Long

    //Get AgentEntity by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    suspend fun getAgentById(id: Int): AgentEntity

    //Update AgentEntity
    @Update
    suspend fun updateAgent(agent: AgentEntity)
}