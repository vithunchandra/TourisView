package com.mdp.tourisview.ui.main.destination

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.history.HistoryFragmentViewState
import com.mdp.tourisview.ui.main.home.HomeFragmentViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DestinationFragmentViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel(){
    val data = MutableLiveData<DestinationFragmentData>()

    fun toggleBookmark(){
        viewModelScope.launch { destinationRepository.toggleDestinationBookmark(data.value!!.id) }
    }
}

class DestinationFragmentViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DestinationFragmentViewModel::class.java) -> {
                DestinationFragmentViewModel(
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
                        Injection.provideDestinationRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as DestinationFragmentViewModelFactory
        }
    }
}