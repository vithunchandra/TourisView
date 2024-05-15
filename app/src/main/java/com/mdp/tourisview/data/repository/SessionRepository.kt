package com.mdp.tourisview.data.repository

import androidx.datastore.dataStore
import com.mdp.tourisview.data.local.pref.UserModel
import com.mdp.tourisview.data.local.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class SessionRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    companion object{
        @Volatile
        private var instance: SessionRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): SessionRepository{
            return instance ?: synchronized(this){
                instance ?: SessionRepository(userPreference)
            }.also { instance = it }
        }
    }
}