package com.mdp.tourisview.ui.authorization.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.local.pref.UserModel
import com.mdp.tourisview.data.repository.AuthRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.authorization.signup.SignUpViewModel
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SignInViewModel(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    var email = ""
    var password = ""
    private val _viewState = MutableLiveData(
        SignInViewState(
            isLoading = false,
            isError = false,
            errorMessage = "",
            data = null,
        )
    )
    val viewState: LiveData<SignInViewState> = _viewState

    fun login(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isError = false,
                errorMessage = ""
            )
            when(val result = authRepository.signin(email, password)){
                is ApiResult.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        data = null,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is ApiResult.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        data = result.data,
                        isError = false,
                        errorMessage = ""
                    )
                }
            }
        }
    }

    fun saveSession(){
        val tempEmail = _viewState.value?.data?.email
        val tempName = _viewState.value?.data?.email
        if(tempEmail != null && tempName != null){
            val user = UserModel(
                email = tempEmail,
                displayName = tempName,
                isLogin = true
            )
            viewModelScope.launch {
                sessionRepository.saveSession(user)
            }
        }
    }

    fun getSession(): LiveData<UserModel>{
        return sessionRepository.getSession().asLiveData()
    }
}

class SignInViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(
                    sessionRepository = sessionRepository,
                    authRepository = authRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: SignInViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): SignInViewModelFactory{
            if(INSTANCE == null){
                synchronized(SignInViewModelFactory::class.java){
                    INSTANCE ?: SignInViewModelFactory(
                        Injection.provideSessionRepository(context),
                        Injection.provideAuthRepository()
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as SignInViewModelFactory
        }
    }
}