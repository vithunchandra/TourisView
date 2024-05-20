package com.mdp.tourisview.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdp.tourisview.data.local.room.dao.DestinationDao
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.local.room.model.RoomReview

@Database(
    entities = [RoomDestination::class, RoomReview::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun destinationDao(): DestinationDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}