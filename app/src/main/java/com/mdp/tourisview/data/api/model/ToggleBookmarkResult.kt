package com.mdp.tourisview.data.api.model

import com.google.gson.annotations.SerializedName

data class ToggleBookmarkResult(
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("isBookmarked")
    val isBookmarked: Boolean
)