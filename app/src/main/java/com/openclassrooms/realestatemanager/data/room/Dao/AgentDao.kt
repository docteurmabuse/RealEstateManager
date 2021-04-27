package com.openclassrooms.realestatemanager.data.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.models.Agent

@Dao
interface AgentDao {

    //Insert Agent
    @Insert
    suspend fun insertAgent(agent: Agent): Long

    //Get Agent by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    suspend fun getAgentById(id: Int): Agent

    //Update Agent
    @Update
    suspend fun updateAgent(agent: Agent)
}