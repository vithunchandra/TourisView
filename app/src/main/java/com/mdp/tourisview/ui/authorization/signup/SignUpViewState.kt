package com.mdp.tourisview.ui.authorization.signup

import com.mdp.tourisview.data.api.model.SignUpResult

data class SignUpViewState(
    var isLoading: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: SignUpResult?
)