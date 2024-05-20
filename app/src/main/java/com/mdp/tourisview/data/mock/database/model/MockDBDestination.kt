package com.mdp.tourisview.data.mock.database.model

import com.mdp.tourisview.data.mock.server.model.MockServerDestination

data class MockDBDestination(
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

fun MockDBDestination.convertToMockServerDestination(): MockServerDestination{
    return MockServerDestination(
        id, poster, name, imageUrl, description,
        latitude, longitude, locationName = locationName,
        createdAt, false
    )
}