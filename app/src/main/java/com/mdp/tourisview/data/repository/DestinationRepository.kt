package com.mdp.tourisview.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.mdp.tourisview.data.api.ApiService
import com.mdp.tourisview.data.local.room.dao.DestinationDao
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.local.room.model.RoomReview
import com.mdp.tourisview.data.mock.server.MockServer
import com.mdp.tourisview.data.mock.server.model.convertToLocalDestination
import com.mdp.tourisview.util.ApiResult
import com.mdp.tourisview.util.getAddress
import java.util.Date
import java.util.UUID

class DestinationRepository private constructor(
    private val apiService: ApiService,
    private val destinationDao: DestinationDao
) {
//    suspend fun uploadDestination(
//        name: String, image: Uri, description: String,
//        latitude: Double, longitude: Double, poster: String
//    ): ApiResult<UploadDestinationResult>{
//        return try{
//            val result = MockDB.uploadDestination(
//                name = name, image = image,
//                description = description, latitude = latitude,
//                longitude = longitude, poster = poster
//            )
//            ApiResult.Success(result)
//        }catch (exc: Exception){
//            ApiResult.Error(exc.message ?: "Upload failed")
//        }
//    }
//
//    suspend fun getAllDestinations(name: String? = null): ApiResult<List<Destination>>{
//        return try{
//            val result = MockDB.getAllDestinations(name)
//            ApiResult.Success(result)
//        }catch (exc: Exception){
//            ApiResult.Error(exc.message ?: "Failed to fetch destinations")
//        }
//    }

    suspend fun insertDestination(
        name: String, image: String, description: String,
        latitude: Double, longitude: Double,
        locationName: String, poster: String
    ): ApiResult<String>{
        return try{
            val roomDestination = RoomDestination(
                id = "DES_${UUID.randomUUID()}",
                poster = poster, name = name,
                imageUrl = image, description = description,
                latitude = latitude, longitude = longitude,
                locationName = locationName,
                createdAt = Date().toString(), false
            )
            apiService.uploadDestination(
                name = name,
                image = image,
                description = description,
                latitude = latitude,
                longitude = longitude,
                locationName = locationName,
                poster = poster
            )
            destinationDao.insertDestination(roomDestination)
            ApiResult.Success("Destination added successfully")
        }catch(exc: Exception){
            ApiResult.Error("Failed to add destination")
        }
    }

    suspend fun insertAllDestinations(roomDestinations: List<RoomDestination>){
        destinationDao.insertAllDestinations(roomDestinations)
    }

    suspend fun toggleDestinationBookmark(id: String): ApiResult<String>{
        return try {
            destinationDao.toggleBookmark(id)
            MockServer.toggleDestinationBookmark(id)
            ApiResult.Success("Bookmarked success")
        }catch(exc: Exception){
            ApiResult.Error("Failed to bookmarked the destination")
        }
    }

    fun getDestinations(name: String): LiveData<List<RoomDestination>>{
        return destinationDao.getDestinations(name)
    }

    fun getAllDestinations(): LiveData<List<RoomDestination>>{
        return destinationDao.getAllDestinations()
    }

    fun getBookmarkedDestinations(): LiveData<List<RoomDestination>>{
        return destinationDao.getBookmarkedDestination()
    }

    suspend fun fetchDestinationsFromServer(): ApiResult<String>{
        return try {
//            val result = MockServer.getAllDestinations()
            val result = apiService.getAllDestinations()
            destinationDao.deleteDestinations()
            destinationDao.insertAllDestinations(
                result.map { it.convertToLocalDestination() }
            )
            ApiResult.Success("Sync success")
        }catch(exc: Exception) {
            ApiResult.Error("Sync failed")
        }
    }

    suspend fun insertReview(
        destinationId: String, reviewText: String, star: Int
    ): ApiResult<String>{
        return try{
            val roomReview = RoomReview(
                id = "REV_${UUID.randomUUID()}",
                destinationId = destinationId,
                reviewText = reviewText,
                star = star
            )
            destinationDao.insertReview(roomReview)
            ApiResult.Success("Review added successfully")
        }catch(exc: Exception){
            ApiResult.Error("Failed to add review")
        }
    }

    suspend fun getReviews(destinationId: String): LiveData<List<RoomReview>>{
        return destinationDao.getReviews(destinationId)
    }

    companion object{
        @Volatile
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService,
            destinationDao: DestinationDao
        ): DestinationRepository{
            return instance ?: synchronized(this){
                instance ?: DestinationRepository(apiService, destinationDao)
            }.also { instance = it }
        }
    }
}