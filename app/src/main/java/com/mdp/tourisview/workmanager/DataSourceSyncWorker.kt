package com.mdp.tourisview.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mdp.tourisview.data.mock.server.model.convertToLocalDestination
import com.mdp.tourisview.di.Injection

//class DataSourceSyncWorker(
//    appContext: Context,
//    workerParams: WorkerParameters
//): CoroutineWorker(appContext, workerParams) {
//    override suspend fun doWork(): Result {
//        val destinationRepository = Injection.provideDestinationRepository(context = applicationContext)
//        val networkRepository = Injection.provideNetworkRepository()
//
//        return try {
//            val apiResult = networkRepository.getAllDestinationsNetwork()
//            val roomDestinations = apiResult.map {
//                it.convertToLocalDestination()
//            }
//            destinationRepository.insertAllDestinations(roomDestinations)
//            Result.success()
//        }catch (exc: Exception){
//            Result.retry()
//        }
//
//    }
//}

//class DataSourceWorkerFactory(
//    private val networkRepository: NetworkRepository
//): WorkerFactory(){
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker? {
//        return when(workerClassName){
//            DataSourceSyncWorker::class.java.name -> {
//                DataSourceSyncWorker(appContext, workerParameters, networkRepository)
//            }
//        }
//    }
//
//}