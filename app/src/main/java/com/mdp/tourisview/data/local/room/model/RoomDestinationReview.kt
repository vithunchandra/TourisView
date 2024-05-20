package com.mdp.tourisview.data.local.room.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity(tableName = "destination_review")
data class RoomDestinationReview(
    @Embedded
    val roomDestination: RoomDestination,

    @Relation(
        parentColumn = "id",
        entityColumn = "destinationId"
    )
    val roomReview: List<RoomReview>
)
