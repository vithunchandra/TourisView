package com.mdp.tourisview.ui.main.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.data.repository.SessionRepository
import com.mdp.tourisview.di.Injection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class HomeFragmentViewModel(
    private val destinationRepository: DestinationRepository,
    private val sessionRepository: SessionRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        HomeFragmentViewState(
            isLoading = false, isSuccess = false,
            isError = false, errorMessage = "",
            data = null
        )
    )

    val viewState: LiveData<HomeFragmentViewState> = _viewState

    fun fetchDestinations(){
        viewModelScope.launch {
            val session = runBlocking {
                sessionRepository.getSession().first()
            }
            val email = session.email
            destinationRepository.fetchDestinationsFromServer(email)
            Log.d("Test Fetching", "Hallo")
        }
    }

    fun toggleBookmark(id: Int){
        viewModelScope.launch {
            val session = runBlocking {
                sessionRepository.getSession().first()
            }
            val email = session.email
            destinationRepository.toggleDestinationBookmark(id, email)
        }
    }

    fun getAllDestinations(name: String? = null): LiveData<List<RoomDestination>>{
        return if(name == null){
            destinationRepository.getAllDestinations()
        }else {
            destinationRepository.getDestinations(name)
        }
    }

//    fun getAllDestinations(name: String? = null){
//        viewModelScope.launch {
//            _viewState.value = _viewState.value?.copy(
//                isLoading = true, isSuccess = false, isError = false
//            )
//
//            when(val result = destinationRepository.getAllDestinations(name)){
//                is ApiResult.Error -> {
//                    _viewState.value = _viewState.value?.copy(
//                        isLoading = false, isSuccess = false,
//                        isError = true, errorMessage = result.message
//                    )
//                }
//                is ApiResult.Success -> {
//                    _viewState.value = _viewState.value?.copy(
//                        isLoading = false, isSuccess = true,
//                        isError = false, errorMessage = "",
//                        data = result.data
//                    )
//                }
//            }
//        }
//    }


}

class HomeFragmentViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository,
    private val sessionRepository: SessionRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java) -> {
                HomeFragmentViewModel(
                    destinationRepository,
                    sessionRepository
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
                        Injection.provideDestinationRepository(context),
                        Injection.provideSessionRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as HomeFragmentViewModelFactory
        }
    }
}