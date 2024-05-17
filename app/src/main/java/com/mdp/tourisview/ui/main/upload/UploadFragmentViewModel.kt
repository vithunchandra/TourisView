package com.mdp.tourisview.ui.main.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.repository.AuthRepository
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.authorization.signup.SignUpViewModel
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class UploadFragmentViewModel(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        UploadFragmentViewState(
            isLoading = false, isError = false,
            isSuccess = false, errorMessage = "",
            name = "", description = "",
            latitude = 0.0, longitude = 0.0,
            imageUri = null
        )
    )

    val viewState: LiveData<UploadFragmentViewState> = _viewState

    fun upload(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true, isError = false,
                isSuccess = false, errorMessage = ""
            )

            _viewState.value?.let {
                val session = runBlocking {
                    sessionRepository.getSession().first()
                }
                val poster = session.email
                Log.d("UploadFragmentViewModel", "poster = $poster")

                if(it.name.isEmpty() || it.imageUri == null || it.description.isEmpty() ||
                    it.latitude == 0.0 || it.longitude == 0.0 || poster.isEmpty()
                ){
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false, isError = true, errorMessage = "Invalid Input"
                    )
                    return@launch
                }
                val result = destinationRepository.uploadDestination(
                    name = it.name , image = it.imageUri!!, description = it.description,
                    latitude = it.latitude, longitude = it.longitude, poster = poster!!
                )

                when(result){
                    is ApiResult.Error -> {
                        _viewState.value = _viewState.value?.copy(
                            isLoading = false, isError = true,
                            isSuccess = false, errorMessage = result.message
                        )
                    }
                    is ApiResult.Success -> {
                        _viewState.value = _viewState.value?.copy(
                            isLoading = false, isError = false,
                            isSuccess = true, errorMessage = "",
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String){
        _viewState.value = _viewState.value?.copy(name = name)
    }

    fun setDescription(description: String){
        _viewState.value = _viewState.value?.copy(description = description)
    }

    fun setLatitude(latitude: Double){
        _viewState.value = _viewState.value?.copy(latitude = latitude)
    }

    fun setLongitude(longitude: Double){
        _viewState.value = _viewState.value?.copy(longitude = longitude)
    }

    fun setImageUri(uri: Uri){
        _viewState.value = _viewState.value?.copy(imageUri = uri)
    }
}

class UploadFragmentViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadFragmentViewModel::class.java) -> {
                UploadFragmentViewModel(
                    sessionRepository,
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UploadFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): UploadFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(UploadFragmentViewModelFactory::class.java){
                    INSTANCE ?: UploadFragmentViewModelFactory(
                        Injection.provideSessionRepository(context),
                        Injection.provideDestinationRepository()
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as UploadFragmentViewModelFactory
        }
    }
}