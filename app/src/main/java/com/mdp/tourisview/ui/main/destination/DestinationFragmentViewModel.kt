package com.mdp.tourisview.ui.main.destination

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.history.HistoryFragmentViewState
import com.mdp.tourisview.ui.main.home.HomeFragmentViewModel
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class DestinationFragmentViewModel(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository
) : ViewModel(){
    val data = MutableLiveData<DestinationFragmentData>()

    private val _viewState = MutableLiveData(
        DestinationFragmentViewState(
            isLoading = false, isSuccess = false,
            isError = false, errorMessage = "",
            data = null
        )
    )

    val viewState: LiveData<DestinationFragmentViewState> = _viewState

    fun toggleBookmark(){
        viewModelScope.launch { destinationRepository.toggleDestinationBookmark(data.value!!.id) }
    }

    fun getAllReview(destinationID: String){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true, isSuccess = false, isError = false
            )

            when(val result = destinationRepository.getAllReviews(destinationID)){
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

    fun sendReview(destinationId: String, reviewText: String, star: Int){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true, isSuccess = false, isError = false
            )

            val session = runBlocking {
                sessionRepository.getSession().first()
            }

            when(val result = destinationRepository.insertReview(session.email, destinationId, reviewText, star)){
                is ApiResult.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false, isSuccess = false,
                        isError = true, errorMessage = result.message
                    )
                }
                is ApiResult.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false, isSuccess = true,
                        isError = false, errorMessage = ""
                    )
                }
            }
        }
    }
}

class DestinationFragmentViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DestinationFragmentViewModel::class.java) -> {
                DestinationFragmentViewModel(
                    sessionRepository,
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: DestinationFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): DestinationFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(DestinationFragmentViewModelFactory::class.java){
                    INSTANCE ?: DestinationFragmentViewModelFactory(
                        Injection.provideSessionRepository(context),
                        Injection.provideDestinationRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as DestinationFragmentViewModelFactory
        }
    }
}