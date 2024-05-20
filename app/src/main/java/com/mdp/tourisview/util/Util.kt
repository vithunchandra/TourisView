package com.mdp.tourisview.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

fun getAddress(latLng: LatLng, context: Context): String{
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

    return if(addresses == null){
        "Location not found"
    }else if(addresses.isEmpty()){
        "Location not found"
    }else {
        val address = addresses[0]
        val city = address.locality
        val country = address.countryName
        "$city, $country"
    }

}