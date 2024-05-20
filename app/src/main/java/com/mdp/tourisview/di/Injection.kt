package com.mdp.tourisview.di

import android.content.Context
import com.mdp.tourisview.data.api.ApiConfig
import com.mdp.tourisview.data.local.pref.UserPreference
import com.mdp.tourisview.data.local.pref.dataStore
import com.mdp.tourisview.data.local.room.AppDatabase
import com.mdp.tourisview.data.repository.AuthRepository
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.NetworkRepository
import com.mdp.tourisview.data.repository.SessionRepository

object Injection{
    fun provideSessionRepository(context: Context): SessionRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        return SessionRepository.getInstance(pref)
    }

    fun provideAuthRepository(): AuthRepository{
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }

    fun provideDestinationRepository(context: Context): DestinationRepository{
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getDatabase(context)
        return DestinationRepository.getInstance(apiService, database.destinationDao())
    }

    fun provideNetworkRepository(): NetworkRepository{
        val apiService = ApiConfig.getApiService()
        return NetworkRepository.getInstance(apiService)
    }
}