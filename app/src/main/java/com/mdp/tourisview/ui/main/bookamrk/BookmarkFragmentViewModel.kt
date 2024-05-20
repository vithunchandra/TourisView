package com.mdp.tourisview.ui.main.bookamrk

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.repository.DestinationRepository
import com.mdp.tourisview.di.Injection
import com.mdp.tourisview.ui.main.home.HomeFragmentViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class BookmarkFragmentViewModel(
    private val destinationRepository: DestinationRepository
): ViewModel() {
    init {
        fetchDestination()
    }
    fun fetchDestination(){
        viewModelScope.launch { destinationRepository.fetchDestinationsFromServer() }
    }

    fun toggleBookmark(id: String){
        viewModelScope.launch {
            destinationRepository.toggleDestinationBookmark(id)
        }
    }

    fun getBookmarkedDestinations(): LiveData<List<RoomDestination>>{
        return destinationRepository.getBookmarkedDestinations()
    }
}

class BookmarkFragmentViewModelFactory private constructor(
    private val destinationRepository: DestinationRepository
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BookmarkFragmentViewModel::class.java) -> {
                BookmarkFragmentViewModel(
                    destinationRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: BookmarkFragmentViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): BookmarkFragmentViewModelFactory{
            if(INSTANCE == null){
                synchronized(BookmarkFragmentViewModelFactory::class.java){
                    INSTANCE ?: BookmarkFragmentViewModelFactory(
                        Injection.provideDestinationRepository(context)
                    ).also { INSTANCE = it }
                }
            }
            return INSTANCE as BookmarkFragmentViewModelFactory
        }
    }
}