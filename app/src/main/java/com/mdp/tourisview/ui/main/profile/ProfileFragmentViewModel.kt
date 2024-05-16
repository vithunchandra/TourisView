package com.mdp.tourisview.ui.main.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.local.pref.UserModel
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ProfileFragmentViewModel(private val sessionRepository: SessionRepository): ViewModel() {
    fun getSession(): LiveData<UserModel>{
        return sessionRepository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            sessionRepository.logout()
        }
    }
}

class ProfileFragmentViewModelFactory(
    private val sessionRepository: SessionRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java) -> {
                ProfileFragmentViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ProfileFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ProfileFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(ProfileFragmentViewModelFactory::class.java){
                    INSTANCE ?: ProfileFragmentViewModelFactory(
                        Injection.provideSessionRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as ProfileFragmentViewModelFactory
        }
    }
}