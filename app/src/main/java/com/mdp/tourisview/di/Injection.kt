package com.mdp.tourisview.di

import android.content.Context
import com.mdp.tourisview.data.api.ApiConfig
import com.mdp.tourisview.data.local.pref.UserPreference
import com.mdp.tourisview.data.local.pref.dataStore
import com.mdp.tourisview.data.repository.AuthRepository
import com.mdp.tourisview.data.repository.DestinationRepository
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

    fun provideDestinationRepository(): DestinationRepository{
        val apiService = ApiConfig.getApiService()
        return DestinationRepository.getInstance(apiService)
    }
}