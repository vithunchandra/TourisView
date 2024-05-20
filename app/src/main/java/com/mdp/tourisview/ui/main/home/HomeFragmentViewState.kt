package com.mdp.tourisview.ui.main.home

import com.mdp.tourisview.data.local.room.model.RoomDestination

data class HomeFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: List<RoomDestination>?
)
