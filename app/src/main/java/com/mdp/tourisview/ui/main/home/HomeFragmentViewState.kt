package com.mdp.tourisview.ui.main.home

import com.mdp.tourisview.data.mock.model.Destination

data class HomeFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: List<Destination>?
)
