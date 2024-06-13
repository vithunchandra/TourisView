package com.mdp.tourisview.data.api.model

import com.mdp.tourisview.data.mock.server.model.MockServerReview

data class InsertReviewResult (
    val message: String,
    val data: MockServerReview
)