package com.mdp.tourisview.data.mock.server.model

import com.google.gson.annotations.SerializedName

data class MockServerReview(
    @field:SerializedName("review_id")
    val review_id: Int,
    @field:SerializedName("reviewer")
    val reviewer: String,
    @field:SerializedName("destination_id")
    val destination_id: Int,
    @field:SerializedName("review")
    val review: String,
    @field:SerializedName("star")
    val star: Int
)
