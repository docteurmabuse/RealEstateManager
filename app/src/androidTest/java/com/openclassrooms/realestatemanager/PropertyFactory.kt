package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.db.model.property.PhotoEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntity
import com.openclassrooms.realestatemanager.db.model.property.PropertyEntityAggregate
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object PropertyFactory {
    fun makeProperty(): PropertyEntityAggregate {
        return PropertyEntityAggregate(
            makeRandomProperty(),

            )
    }

    private fun makeRandomMedia(): List<PhotoEntity> {

    }

    private fun makeRandomProperty(): PropertyEntity {
        return PropertyEntity(
            makeRandomInt(),
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
            makeRandomInt(),
        )
    }

    private fun makeRandomPhoto(): PhotoEntity {
        return PhotoEntity(
            makeRandomInt(),
            makeRandomInt(),
            makeRandomString(),
            makeRandomString(),
        )
    }

    private fun makeRandomVideo(): PhotoEntity {
        return PhotoEntity(
            makeRandomInt(),
            makeRandomInt(),
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