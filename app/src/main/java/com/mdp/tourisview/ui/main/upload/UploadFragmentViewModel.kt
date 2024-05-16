package com.mdp.tourisview.ui.main.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UploadFragmentViewModel: ViewModel() {
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