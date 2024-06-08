package com.mdp.tourisview.ui.main.history

import com.mdp.tourisview.data.mock.server.model.MockServerDestination

data class HistoryFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: List<MockServerDestination>?
)
