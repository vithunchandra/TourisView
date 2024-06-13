package com.mdp.tourisview.data.api.model

import com.google.gson.annotations.SerializedName
import com.mdp.tourisview.data.mock.server.model.MockServerReview

data class InsertReviewResult (
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: MockServerReview
)