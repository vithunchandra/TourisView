package com.mdp.tourisview.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.local.room.model.RoomDestinationReview
import com.mdp.tourisview.data.local.room.model.RoomReview

@Dao
interface DestinationDao {
    @Upsert
    suspend fun insertDestination(roomDestination: RoomDestination)

    @Insert
    suspend fun insertAllDestinations(roomDestinations: List<RoomDestination>)

    @Query("UPDATE destination SET isBookmarked = NOT isBookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: Int)

    @Query("DELETE FROM destination")
    suspend fun deleteDestinations()

    @Upsert
    suspend fun insertReview(roomReview: RoomReview)

    @Query("SELECT * FROM destination WHERE name LIKE '%'|| :name ||'%'")
    fun getDestinations(name: String): LiveData<List<RoomDestination>>

    @Query("SELECT * FROM review WHERE destinationId = :destinationId")
    fun getReviews(destinationId: String): LiveData<List<RoomReview>>

    @Query("SELECT * FROM destination")
    fun getAllDestinations(): LiveData<List<RoomDestination>>

    @Query("SELECT * FROM review")
    fun getAllReviews(): LiveData<List<RoomReview>>

    @Query("SELECT * FROM destination WHERE id = :id")
    fun getDestination(id: String): LiveData<RoomDestination>

    @Query("SELECT * FROM destination WHERE isBookmarked = true")
    fun getBookmarkedDestination(): LiveData<List<RoomDestination>>

    @Query("SELECT * FROM review WHERE id = :id")
    fun getReview(id: String): LiveData<RoomReview>

    @Transaction
    @Query("SELECT * FROM destination")
    fun getAllDestinationReview(): LiveData<List<RoomDestinationReview>>
}