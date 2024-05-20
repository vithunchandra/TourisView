package com.mdp.tourisview.data.mock.server

import android.content.Context
import android.util.Log
import androidx.camera.core.impl.utils.ContextUtil
import com.google.android.gms.maps.model.LatLng
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.data.mock.server.model.User
import com.mdp.tourisview.util.getAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.util.Date
import java.util.UUID
import kotlin.random.Random

object MockServer {
    private val users: MutableList<User> = mutableListOf()
    private val destinations: MutableList<MockServerDestination> = mutableListOf()

    suspend fun initServer(context: Context){
        coroutineScope {
            for(i in 1..100){
                val user = User(
                    email = "user_$i",
                    displayName = "User_$i",
                    password = "pass_user_$i"
                )
                users.add(user)
            }

            for(user in users){
                val latitude = Random.nextDouble(0.0, 99.0)
                val longitude = Random.nextDouble(0.0, 99.0)
                val location = getAddress(LatLng(latitude, longitude), context)
                val destination = MockServerDestination(
                    id = "DES_${UUID.randomUUID()}",
                    poster = user.email,
                    name = "${user.displayName} Destination",
                    imageUrl = "https://picsum.photos/id/${Random.nextInt(1, 300)}/200/300",
                    description = "${user.displayName} Destination Description",
                    latitude = latitude,
                    longitude = longitude,
                    locationName = location,
                    createdAt = Date().toString(),
                    isBookmarked = Random.nextBoolean()
                )
                destinations.add(destination)
            }
        }
    }

    suspend fun uploadDestination(
        name: String, image: String, description: String,
        latitude: Double, longitude: Double, locationName: String, poster: String
    ): UploadDestinationResult {
        delay(2000)
        val newDestination = MockServerDestination(
            id = "DES_${UUID.randomUUID()}",
            name = name, imageUrl = image,
            description = description,
            latitude = latitude,
            longitude = longitude,
            locationName = locationName,
            poster = poster,
            createdAt = Date().toString(),
            isBookmarked = false
        )
        destinations.add(newDestination)
        return UploadDestinationResult(
            message = "Destination uploaded successfully",
            data = newDestination
        )
    }

    suspend fun toggleDestinationBookmark(id: String){
        delay(500)
        val destination = destinations.find {
            it.id == id
        }
        if (destination != null) {
            Log.d("Tes toggle", destination.name)
        }
        Log.d("Tes toggle", "Before: ${destination?.isBookmarked}")
        destination?.let {
            it.isBookmarked = !it.isBookmarked
        }
        Log.d("Tes toggle", "After: ${destination?.isBookmarked}")

    }

    suspend fun getAllDestinations(name: String? = null): List<MockServerDestination>{
        delay(2000)
        val result = if(name != null){
            destinations.filter {
                it.name.contains(name)
            }
        }else destinations
        return result
    }
}