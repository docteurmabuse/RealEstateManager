package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    //Insert AgentEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity): Long

    //Get All AgentEntity
    @Query("SELECT * FROM agent ORDER BY name DESC")
    fun getAllAgent(): Flow<List<AgentEntity>>

    //Get AgentEntity by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentById(id: String): Flow<AgentEntity>

    //Update AgentEntity
    @Update
    suspend fun updateAgent(agent: AgentEntity)
}