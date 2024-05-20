package com.mdp.tourisview.ui.main.map.view_location

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.di.Injection
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DestinationMapLocationViewModel(
    private val destinationRepository: DestinationRepository
): ViewModel() {

    init {
        fetchAllDestinations()
    }
    fun fetchAllDestinations(){
        viewModelScope.launch { destinationRepository.fetchDestinationsFromServer() }
    }

    fun getAllDestinations(name: String? = null): LiveData<List<RoomDestination>> {
        return if(name == null){
            destinationRepository.getAllDestinations()
        }else {
            destinationRepository.getDestinations(name)
        }
    }
}

class DestinationMapLocationViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DestinationMapLocationViewModel::class.java) -> {
                DestinationMapLocationViewModel(
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: DestinationMapLocationViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): DestinationMapLocationViewModelFactory{
            if(INSTANCE == null){
                synchronized(DestinationMapLocationViewModelFactory::class.java){
                    INSTANCE ?: DestinationMapLocationViewModelFactory(
                        Injection.provideDestinationRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as DestinationMapLocationViewModelFactory
        }
    }
}