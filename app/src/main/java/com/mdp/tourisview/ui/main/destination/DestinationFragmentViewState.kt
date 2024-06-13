package com.mdp.tourisview.ui.main.destination

import com.mdp.tourisview.data.mock.server.model.MockServerReview

data class DestinationFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: List<MockServerReview>?
)
