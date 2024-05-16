package com.mdp.tourisview.ui.main.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.ActivitySelectLocationBinding
import java.util.concurrent.TimeUnit

class SelectLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySelectLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLocation()
        buildLocationCallback()
        buildLocationRequest()
//        if(checkForegroundAndBackgroundLocationPermission()){
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//        }
        setUpView()
        setUpAction()
    }

    private fun buildLocationRequest(){
        val priority = if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            Priority.PRIORITY_HIGH_ACCURACY
        }else Priority.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest = LocationRequest.Builder(priority, TimeUnit.SECONDS.toMillis(3)).build()
    }

    private fun buildLocationCallback(){
        locationCallback = object: LocationCallback(){
            override fun onLocationResult(location: LocationResult) {
                mMap.clear()
                val latLon = LatLng(location.lastLocation?.latitude!!, location.lastLocation?.longitude!!)
                mMap.addMarker(
                    MarkerOptions().
                    position(latLon).
                    title("${latLon.latitude}, ${latLon.longitude}")
                )
                latitude = location.lastLocation?.latitude!!
                longitude = location.lastLocation?.longitude!!
                binding.selectButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpView(){
        mMap.setOnMapLongClickListener { location ->
            mMap.clear()
            mMap.addMarker(
                MarkerOptions().
                position(location).
                title("${location.latitude}, ${location.longitude}")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            latitude = location.latitude
            longitude = location.longitude
            binding.selectButton.visibility = View.VISIBLE
        }
    }

    private fun setUpAction(){
        binding.selectButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra(LATITUDE, latitude)
            intent.putExtra(LONGITUDE, longitude)
            setResult(LOCATION_RESULT, intent)
            finish()
        }
    }

    private fun checkPermission(permission: String): Boolean{
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkForegroundAndBackgroundLocationPermission(): Boolean{
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundLocationApproved = checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        return foregroundLocationApproved || backgroundLocationApproved
    }

    private fun getMyLocation(){
        if(checkForegroundAndBackgroundLocationPermission()){
            mMap.isMyLocationEnabled = true
        }
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val LOCATION_RESULT = 200
    }
}