package com.mdp.tourisview.ui.authorization.signin

import com.mdp.tourisview.data.api.model.SignInResult

data class SignInViewState(
    var isLoading: Boolean,
    var data: SignInResult?,
    var isError: Boolean,
    var errorMessage: String
)
