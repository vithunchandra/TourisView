package com.mdp.tourisview.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.mdp.tourisview.data.local.pref.UserModel
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.profile.ProfileFragmentViewModel
import java.lang.IllegalArgumentException

class MainActivityViewModel(private val sessionRepository: SessionRepository): ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return sessionRepository.getSession().asLiveData()
    }
}

class MainActivityViewModelFactory(
    private val sessionRepository: SessionRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> {
                MainActivityViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: MainActivityViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): MainActivityViewModelFactory{
            if(INSTANCE == null){
                synchronized(MainActivityViewModelFactory::class.java){
                    INSTANCE ?: MainActivityViewModelFactory(
                        Injection.provideSessionRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as MainActivityViewModelFactory
        }
    }
}