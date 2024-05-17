package com.mdp.tourisview.data.mock.model

import android.net.Uri

data class Destination(
    val poster: String,
    val name: String,
    var imageUri: Uri,
    val description: String,
    var latitude: Double,
    var longitude: Double,
    var like: Int,
)
