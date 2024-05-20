package com.mdp.tourisview.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class RoomReview(
    @PrimaryKey
    val id: String,
    val destinationId: String,
    val reviewText: String,
    val star: Int
)