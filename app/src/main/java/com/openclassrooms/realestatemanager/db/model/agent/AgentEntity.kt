package com.openclassrooms.realestatemanager.db.model.agent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.domain.model.agent.Agent
import java.util.*

@Entity(
    tableName = "agent"
)
data class AgentEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String? = UUID.randomUUID().toString(),
    var name: String?,
    var email: String?,
    var phone: String?,
    var photoUrl: String?
) {
    companion object {
        fun fromDomain(domainModel: Agent): AgentEntity {
            return AgentEntity(
                id = domainModel.id,
                name = domainModel.name,
                email = domainModel.email,
                phone = domainModel.phone,
                photoUrl = domainModel.photoUrl
            )
        }
    }

    fun toDomain(): Agent {
        return Agent(
            id,
            name,
            email,
            phone,
            photoUrl
        )
    }
}
