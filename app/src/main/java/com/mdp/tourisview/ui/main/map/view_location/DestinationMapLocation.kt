package com.mdp.tourisview.ui.main.map.view_location

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mdp.tourisview.R
import com.mdp.tourisview.databinding.ActivityDestinationMapLocationBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class DestinationMapLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDestinationMapLocationBinding
    private val viewModel by viewModels<DestinationMapLocationViewModel>{
        DestinationMapLocationViewModelFactory.getInstance(this.baseContext)
    }
    private val listMarker = mutableListOf<Marker?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDestinationMapLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLocation()
        setUpState()
    }

    private fun setUpState(){
        viewModel.getAllDestinations().observe(this){destinations ->
            for(destination in destinations){
                val markerView = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.marker_with_image, null)
                val imageView = markerView.findViewById<ImageView>(R.id.image_inside_marker)
                Picasso.get().load(destination.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(imageView, object: Callback{
                        override fun onSuccess() {
                            val bitmap = Bitmap.createScaledBitmap(viewBitMap(markerView), markerView.width, markerView.height, false)
                            val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

                            val destinationLatLng = LatLng(destination.latitude, destination.longitude)
                            val marker = mMap.addMarker(
                                MarkerOptions().position(destinationLatLng).title(destination.name).icon(smallMarkerIcon)
                            )
                            marker?.tag = destination.id

                            mMap.setOnMarkerClickListener {
                                Log.d("Tes Map", "marker_id: ${it.tag} - destination_id: ${destination.id}")
                                val tempDestination = destinations.find { roomDestination ->
                                    roomDestination.id == it.tag
                                }
                                Toast.makeText(baseContext, "Hello dari ${tempDestination?.name}", Toast.LENGTH_SHORT).show()
                                return@setOnMarkerClickListener false
                            }
                        }

                        override fun onError(p0: Exception?) {
                            val bitmap = Bitmap.createScaledBitmap(viewBitMap(markerView), markerView.width, markerView.height, false)
                            val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

                            val destinationLatLng = LatLng(destination.latitude, destination.longitude)
                            mMap.addMarker(
                                MarkerOptions().position(destinationLatLng).title(destination.name).icon(smallMarkerIcon)
                            )
                        }
                    })
            }
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

    private fun viewBitMap(view: View): Bitmap{
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }
}