package com.mdp.tourisview.ui.main.destination

import android.os.Parcelable
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import java.io.Serializable

data class DestinationFragmentData(
    val id: String,
    val poster: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val createdAt: String,
    var isBookmarked: Boolean
): Serializable{

    fun getLocDescription():String{
        return locationName + " ( lat: %.5f".format(latitude) + ", long: %.5f".format(longitude) + " )"
    }

    fun getPosterDescription():String{
        return "posted by $poster"
    }

    companion object{
        fun fromMockServerDestination(data:MockServerDestination):DestinationFragmentData{
            return DestinationFragmentData(
                data.id,
                data.poster,
                data.name,
                data.imageUrl,
                data.description,
                data.latitude,
                data.longitude,
                data.locationName,
                data.createdAt,
                data.isBookmarked
            )
        }

        fun fromRoomDestination(data:RoomDestination):DestinationFragmentData{
            return DestinationFragmentData(
                data.id,
                data.poster,
                data.name,
                data.imageUrl,
                data.description,
                data.latitude,
                data.longitude,
                data.locationName,
                data.createdAt,
                data.isBookmarked
            )
        }
    }
}
