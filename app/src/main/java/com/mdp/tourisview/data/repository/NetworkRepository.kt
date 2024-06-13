//package com.mdp.tourisview.data.repository
//
//import com.mdp.tourisview.data.api.ApiService
//import com.mdp.tourisview.data.mock.server.MockServer
//import com.mdp.tourisview.data.mock.server.model.MockServerDestination
//
//class NetworkRepository private constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getAllDestinationsNetwork(name: String? = null): List<MockServerDestination>{
//        return apiService.getAllDestinations(name)
//    }
//
//    companion object{
//        @Volatile
//        private var instance: NetworkRepository? = null
//        fun getInstance(
//            apiService: ApiService
//        ): NetworkRepository{
//            return instance ?: synchronized(this){
//                instance ?: NetworkRepository(apiService)
//            }.also { instance = it }
//        }
//    }
//}