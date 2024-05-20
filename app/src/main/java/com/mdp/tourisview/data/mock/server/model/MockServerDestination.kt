package com.mdp.tourisview.data.mock.server.model

import com.mdp.tourisview.data.local.room.model.RoomDestination

data class MockServerDestination(
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

fun MockServerDestination.convertToLocalDestination(): RoomDestination{
    return RoomDestination(
        id, poster, name, imageUrl,
        description, latitude, longitude,
        locationName, createdAt, isBookmarked
    )
}
