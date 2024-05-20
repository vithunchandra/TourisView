package com.mdp.tourisview.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "destination")
data class RoomDestination(
    @PrimaryKey
    val id: String,
    val poster: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val createdAt: String,
    var isBookmarked: Boolean
)