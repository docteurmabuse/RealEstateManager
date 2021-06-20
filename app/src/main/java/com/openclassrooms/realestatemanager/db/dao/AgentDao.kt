package com.openclassrooms.realestatemanager.db.dao

import android.database.Cursor
import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    //Insert AgentEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(
        agent: AgentEntity,
    ): Long


    //Get All AgentEntity
    @Query("SELECT * FROM agent ORDER BY name DESC")
    fun getAllAgents(): Flow<List<AgentEntity>>

    //Get AgentEntity by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentById(id: String): Flow<AgentEntity>

    //Update AgentEntity
    @Update
    suspend fun updateAgent(agent: AgentEntity) {

    }

    //Get All AgentEntity
    @Query("SELECT * FROM agent ORDER BY name DESC")
    fun getAllAgentsWithCursor(): Cursor

    //Get AgentEntity by Id
    @Query("SELECT * FROM agent WHERE id = :id")
    fun getAgentByIdWithCursor(id: String): Cursor

    @Transaction
    //Get Properties Count
    @Query(
        "SELECT COUNT(*)  from agent"
    )
    fun getAgentCountWithCursor(): Cursor
}
