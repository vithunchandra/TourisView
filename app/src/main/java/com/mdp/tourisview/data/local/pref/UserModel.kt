package com.mdp.tourisview.data.local.pref

data class UserModel(
    val email: String,
    val displayName: String,
    val isLogin: Boolean = false
)