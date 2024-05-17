package com.mdp.tourisview.ui.main.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.upload.UploadFragmentViewModel
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class HomeFragmentViewModel(
    private val destinationRepository: DestinationRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        HomeFragmentViewState(
            isLoading = false, isSuccess = false,
            isError = false, errorMessage = "",
            data = null
        )
    )

    val viewState: LiveData<HomeFragmentViewState> = _viewState

    init {
        getAllDestinations()
    }

    fun getAllDestinations(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true, isSuccess = false, isError = false
            )

            when(val result = destinationRepository.getAllDestinations()){
                is ApiResult.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false, isSuccess = false,
                        isError = true, errorMessage = result.message
                    )
                }
                is ApiResult.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false, isSuccess = true,
                        isError = false, errorMessage = "",
                        data = result.data
                    )
                }
            }
        }
    }
}

class HomeFragmentViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java) -> {
                HomeFragmentViewModel(
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: HomeFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): HomeFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(HomeFragmentViewModelFactory::class.java){
                    INSTANCE ?: HomeFragmentViewModelFactory(
                        Injection.provideDestinationRepository()
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as HomeFragmentViewModelFactory
        }
    }
}