package com.mdp.tourisview.ui.authorization.signup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.repository.AuthRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SignUpViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        SignUpViewState(
            isLoading = false,
            isError = false,
            errorMessage = "",
            data = null
        )
    )
    val viewState: LiveData<SignUpViewState> = _viewState
//    var email = ""
//    var displayName = ""
//    var password = ""
    val email = MutableLiveData<String>("")
    val displayName = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

   fun signup(){
       viewModelScope.launch {
           _viewState.value = _viewState.value?.copy(
               isLoading = true,
               isError = false,
               errorMessage = ""
           )
           when(val result = authRepository.signup(email.value!!, password.value!!, displayName.value!!)){
               is ApiResult.Error -> {
                   _viewState.value = _viewState.value?.copy(
                       isLoading = false,
                       isError = true,
                       errorMessage = result.message
                   )
               }
               is ApiResult.Success -> {
                   _viewState.value = _viewState.value?.copy(
                       isLoading = false,
                       isError = false,
                       errorMessage = "",
                       data = result.data
                   )
               }
           }
       }
   }
}

class SignUpViewModelFactory private constructor(
    private val authRepository: AuthRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: SignUpViewModelFactory? = null
        @JvmStatic
        fun getInstance(): SignUpViewModelFactory{
            if(INSTANCE == null){
                synchronized(SignUpViewModelFactory::class.java){
                    INSTANCE ?: SignUpViewModelFactory(
                        Injection.provideAuthRepository()
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as SignUpViewModelFactory
        }
    }
}