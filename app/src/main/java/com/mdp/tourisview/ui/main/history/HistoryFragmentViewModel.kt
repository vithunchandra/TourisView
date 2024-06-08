package com.mdp.tourisview.ui.main.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.home.HomeFragmentViewState
import com.mdp.tourisview.util.ApiResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class HistoryFragmentViewModel(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository,
): ViewModel() {
    // TODO: Implement the ViewModel
    private val _viewState = MutableLiveData(
        HistoryFragmentViewState(
            isLoading = false, isSuccess = false,
            isError = false, errorMessage = "",
            data = null
        )
    )

    val viewState: LiveData<HistoryFragmentViewState> = _viewState

    fun toggleBookmark(id: String){
        viewModelScope.launch { destinationRepository.toggleDestinationBookmark(id) }
    }

    fun getAllHistory(){
        val session = runBlocking {
            sessionRepository.getSession().first()
        }
        val email = session.email

        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true, isSuccess = false, isError = false
            )

            when(val result = destinationRepository.getAllHistory(email)){
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

class HistoryFragmentViewModelFactory private constructor(
    private val sessionRepository: SessionRepository,
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryFragmentViewModel::class.java) -> {
                HistoryFragmentViewModel(
                    sessionRepository,
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: HistoryFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): HistoryFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(HistoryFragmentViewModelFactory::class.java){
                    INSTANCE ?: HistoryFragmentViewModelFactory(
                        Injection.provideSessionRepository(context),
                        Injection.provideDestinationRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as HistoryFragmentViewModelFactory
        }
    }
}