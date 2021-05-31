package com.openclassrooms.realestatemanager.db.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.db.model.agent.AgentEntity
import com.openclassrooms.realestatemanager.db.model.property.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPropertyAggregate(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity,
        agentEntity: AgentEntity
    )

    suspend fun insertProperty(propertyWithAgentEntity: PropertyWithAgentEntity) {
        insertPropertyAggregate(
            propertyWithAgentEntity.propertyEntityAggregate.property,
            propertyWithAgentEntity.propertyEntityAggregate.photos,
            propertyWithAgentEntity.propertyEntityAggregate.videos,
            propertyWithAgentEntity.propertyEntityAggregate.address,
            propertyWithAgentEntity.agent

        )
    }

    @Transaction
    //Get PropertyEntity by Id
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: String): PropertyWithAgentEntity

    @Transaction
    //Get PropertiesEntity List
    @Query("SELECT * FROM properties ORDER BY sell_date DESC")
    fun getAllProperties(): Flow<List<PropertyWithAgentEntity>>

    // Retrieve PropertiesEntity List form a query
    @Query("SELECT * FROM properties WHERE surface BETWEEN 200 AND 300 OR price  BETWEEN 1500000 AND 2000000  || :query || '%' ORDER BY sell_date DESC")
    suspend fun searchProperties(
        query: String
    ): List<PropertyEntityAggregate>

    //Update PropertyEntity
    @Update
    suspend fun updatePropertyAggregate(
        property: PropertyEntity,
        photos: List<PhotoEntity>,
        videos: List<VideoEntity>,
        address: AddressEntity,
        agentEntity: AgentEntity
    )

    suspend fun updateProperty(propertyWithAgentEntity: PropertyWithAgentEntity) {
        updatePropertyAggregate(
            propertyWithAgentEntity.propertyEntityAggregate.property,
            propertyWithAgentEntity.propertyEntityAggregate.photos,
            propertyWithAgentEntity.propertyEntityAggregate.videos,
            propertyWithAgentEntity.propertyEntityAggregate.address,
            propertyWithAgentEntity.agent
        )
    }
}
