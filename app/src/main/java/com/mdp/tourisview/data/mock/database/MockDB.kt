package com.mdp.tourisview.data.mock.database

import android.net.Uri
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.database.model.MockDBDestination
import com.mdp.tourisview.data.mock.database.model.User
import com.mdp.tourisview.data.mock.database.model.convertToMockServerDestination
import kotlinx.coroutines.delay
import java.util.Date
import java.util.UUID
import kotlin.random.Random

object MockDB {
    private val users: MutableList<User> = mutableListOf()
    private val destinations: MutableList<MockDBDestination> = mutableListOf()

    suspend fun login(email: String, password: String): SignInResult{
        delay(2000)
        val user = users.first{
            it.email == email && it.password == password
        }
        return SignInResult(
            email = user.email,
            displayName = user.displayName
        )
    }

//    suspend fun login(email: String, password: String): SignInResult{
//        // login using backend
//        val apiService = ApiConfig.getApiService()
//        return try {
//            val response = apiService.login(email, password)
//            SignInResult(response.email, response.displayName)
//        } catch (e: Exception) {
//            throw e
//        }
//
//    }

    suspend fun register(email: String, password: String, displayName: String): SignUpResult{
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

//    suspend fun register(email: String, password: String, displayName: String): SignUpResult{
//        // register using backend
//        val apiService = ApiConfig.getApiService()
//        return try {
//            val response = apiService.register(email, password, displayName)
//            SignUpResult(response.email, response.displayName)
//        } catch (e: Exception) {
//            throw e
//        }
//    }

    suspend fun uploadDestination(
        name: String, image: String, description: String,
        latitude: Double, longitude: Double, poster: String
    ): UploadDestinationResult{
        delay(2000)
        val newDestination = MockDBDestination(
            name = name, imageUrl = image,
            description = description, latitude = latitude,
            longitude = longitude, locationName = "location not found", createdAt = Date().toString(),
            poster = poster, id = Random.nextInt(), isBookmarked = false, avgStar = 5.0
        )
        destinations.add(newDestination)
        return UploadDestinationResult(
            message = "Destination uploaded successfully",
            data = newDestination.convertToMockServerDestination()
        )
    }

    suspend fun getAllDestinations(name: String?): List<MockDBDestination>{
        delay(2000)
        val result = if(name != null){
            destinations.filter {
                it.name.contains(name)
            }
        }else destinations
        return result
    }

//    suspend fun getAllDestinations(): List<Destination>{
//        // Still failed for this
//        // what ImageUri should return?
//        val apiService = ApiConfig.getApiService()
//        return try {
//            val response = apiService.getAllDestination()
//            response
//        } catch (e: Exception) {
//            throw e
//        }
//    }
}