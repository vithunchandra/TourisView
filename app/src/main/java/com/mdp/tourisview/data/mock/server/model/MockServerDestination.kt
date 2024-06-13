package com.mdp.tourisview.data.mock.server.model

import com.google.gson.annotations.SerializedName
import com.mdp.tourisview.data.local.room.model.RoomDestination

data class MockServerDestination(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("poster")
    val poster: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("imageUrl")
    val imageUrl: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("latitude")
    val latitude: Double,
    @field:SerializedName("longitude")
    val longitude: Double,
    @field:SerializedName("locationName")
    val locationName: String,
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("isBookmarked")
    var isBookmarked: Boolean,
    @field:SerializedName("avgStar")
    var avgStar: Double
)

fun MockServerDestination.convertToLocalDestination(): RoomDestination{
    return RoomDestination(
        id, poster, name, imageUrl,
        description, latitude, longitude,
        locationName, createdAt, isBookmarked,
        avgStar
    )
}
