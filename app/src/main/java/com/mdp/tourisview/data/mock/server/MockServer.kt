package com.mdp.tourisview.data.mock.server

import android.content.Context
import android.util.Log
import androidx.camera.core.impl.utils.ContextUtil
import com.google.android.gms.maps.model.LatLng
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.database.MockDB
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.data.mock.server.model.User
import com.mdp.tourisview.util.getAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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
                val location = async(Dispatchers.IO){
                    getAddress(LatLng(latitude, longitude), context)
                }
                withContext(Dispatchers.Main){
                    val destination = MockServerDestination(
                        id = "DES_${UUID.randomUUID()}",
                        poster = user.email,
                        name = "${user.displayName} Destination",
                        imageUrl = "https://picsum.photos/id/${Random.nextInt(1, 300)}/200/300",
                        description = "${user.displayName} Destination Description",
                        latitude = latitude,
                        longitude = longitude,
                        locationName = location.await(),
                        createdAt = Date().toString(),
                        isBookmarked = Random.nextBoolean()
                    )
                    destinations.add(destination)
                }
            }
        }
    }

    suspend fun login(email: String, password: String): SignInResult {
        delay(2000)
        val user = users.first{
            it.email == email && it.password == password
        }
        return SignInResult(
            email = user.email,
            displayName = user.displayName
        )
    }

    suspend fun register(email: String, password: String, displayName: String): SignUpResult {
        delay(2000)
        val user = User(
            email = email,
            password = password,
            displayName = displayName
        )
        users.add(user)
        return SignUpResult(
            email = email,
            displayName = displayName
        )
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

    suspend fun getAllHistory(email:String):List<MockServerDestination>{
        delay(2000)
        val result =
            destinations.filter {
                it.poster == email
            }
        return result
    }
}