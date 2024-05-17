package com.mdp.tourisview.data.mock

import com.mdp.tourisview.data.api.ApiConfig
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.model.Destination
import com.mdp.tourisview.data.mock.model.User
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.delay

object MockDB {
    private val users: MutableList<User> = mutableListOf()
    private val destinations: MutableList<Destination> = mutableListOf()

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
}