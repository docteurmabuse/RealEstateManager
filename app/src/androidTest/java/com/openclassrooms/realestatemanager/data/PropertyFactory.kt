package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import com.openclassrooms.realestatemanager.db.model.property.VideoEntity
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object PropertyFactory {

    private val propertyId: Long = 1

    private val makeRandomPhotoList =
        listOf(makeRandomPhoto(), makeRandomPhoto(), makeRandomPhoto())

    private val makeRandomVideoList: List<VideoEntity> =
        listOf(makeRandomVideo(), makeRandomVideo(), makeRandomVideo())

    fun makeProperty(): PropertyEntityAggregate {
        return PropertyEntityAggregate(
            makeRandomProperty(),
            makeRandomPhotoList,
            makeRandomVideoList
        )
    }

    private fun makeRandomProperty(): PropertyEntity {
        return PropertyEntity(
            propertyId,
            makeRandomString(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomInt(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomInt(),
            makeRandomString(),
            makeRandomString(),
            makeRandomString(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomBoolean(),
            makeRandomLong(),
            makeRandomLong(),
            1,
        )
    }

    private fun makeRandomPhoto(): PhotoEntity {
        return PhotoEntity(
            makeRandomLong(),
            propertyId,
            makeRandomString(),
            makeRandomString(),
        )
    }

    private fun makeRandomVideo(): VideoEntity {
        return VideoEntity(
            makeRandomLong(),
            propertyId,
            makeRandomString(),
            makeRandomString(),
        )
    }

    private fun makeRandomString() = UUID.randomUUID().toString()

    private fun makeRandomInt() =
        ThreadLocalRandom.current().nextInt(0, 1000 + 1)

    private fun makeRandomLong() =
        ThreadLocalRandom.current().nextLong(1619155158, 1619155158 + 1)

    private fun makeRandomBoolean() =
        ThreadLocalRandom.current().nextBoolean()
}