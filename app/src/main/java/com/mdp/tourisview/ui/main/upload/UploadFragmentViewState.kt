package com.mdp.tourisview.ui.main.upload

import android.net.Uri

data class UploadFragmentViewState(
    var isLoading: Boolean,
    var isError: Boolean,
    var isSuccess: Boolean,
    var errorMessage: String,
    var name: String,
    var description: String,
    var latitude: Double,
    var longitude: Double,
    var imageUri: Uri?
)