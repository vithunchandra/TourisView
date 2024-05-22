package com.mdp.tourisview.data.repository

import com.mdp.tourisview.data.api.ApiService
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.mock.database.MockDB
import com.mdp.tourisview.util.ApiResult

class AuthRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun signup(
        email: String,
        password: String,
        displayName: String
    ): ApiResult<SignUpResult>{
        return try{
            val result = apiService.register(email, password, displayName)
            ApiResult.Success(result)
        }catch(exc: Exception){
            ApiResult.Error(exc.message ?: "UnknownError")
        }
    }

    suspend fun signin(
        email: String,
        password: String
    ): ApiResult<SignInResult>{
        return try{
            val result = apiService.login(email, password)
            ApiResult.Success(result)
        }catch(exc: Exception){
            ApiResult.Error(exc.message ?: "UnknownError")
        }
    }

    companion object{
        @Volatile
        var INSTANCE: AuthRepository? = null

        fun getInstance(apiService: ApiService): AuthRepository{
            return if(INSTANCE == null){
                synchronized(AuthRepository::class.java){
                    INSTANCE ?: AuthRepository(apiService)
                        .also { INSTANCE = it }
                }
            }else INSTANCE as AuthRepository
        }
    }
}