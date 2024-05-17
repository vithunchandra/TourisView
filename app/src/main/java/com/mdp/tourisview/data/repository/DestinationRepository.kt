package com.mdp.tourisview.data.repository

import android.net.Uri
import com.mdp.tourisview.data.api.ApiService
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.local.pref.UserPreference
import com.mdp.tourisview.data.mock.MockDB
import com.mdp.tourisview.data.mock.model.Destination
import com.mdp.tourisview.util.ApiResult

class DestinationRepository private constructor(
    val apiService: ApiService
) {
    suspend fun uploadDestination(
        name: String, image: Uri, description: String,
        latitude: Double, longitude: Double, poster: String
    ): ApiResult<UploadDestinationResult>{
        return try{
            val result = MockDB.uploadDestination(
                name = name, image = image,
                description = description, latitude = latitude,
                longitude = longitude, poster = poster
            )
            ApiResult.Success(result)
        }catch (exc: Exception){
            ApiResult.Error(exc.message ?: "Upload failed")
        }
    }

    suspend fun getAllDestinations(): ApiResult<List<Destination>>{
        return try{
            val result = MockDB.getAllDestinations()
            ApiResult.Success(result)
        }catch (exc: Exception){
            ApiResult.Error(exc.message ?: "Failed to fetch destinations")
        }
    }

    companion object{
        @Volatile
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService
        ): DestinationRepository{
            return instance ?: synchronized(this){
                instance ?: DestinationRepository(apiService)
            }.also { instance = it }
        }
    }
}