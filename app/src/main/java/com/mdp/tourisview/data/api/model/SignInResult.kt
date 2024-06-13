package com.mdp.tourisview.data.api.model

import com.google.gson.annotations.SerializedName

data class SignInResult(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("displayName")
    val displayName: String,
)