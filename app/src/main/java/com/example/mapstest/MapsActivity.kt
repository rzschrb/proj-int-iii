package com.example.mapstest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible

import com.example.mapstest.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var showError: MaterialButton
    private lateinit var exitError: MaterialButton
    private lateinit var cardError: MaterialCardView
    private lateinit var cardRouteOfTheDay: MaterialCardView
    private lateinit var cardRoutes: MaterialCardView
    private lateinit var exitRoutes: MaterialButton

    private lateinit var lastLocation: Location

    var fusedLocationProviderClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        showError = findViewById(R.id.showError)
        exitError = findViewById(R.id.exitError)
        cardError = findViewById(R.id.cardError)
        cardRouteOfTheDay = findViewById(R.id.cardRouteOfTheDay)
        cardRoutes = findViewById(R.id.cardRoutes)
        exitRoutes = findViewById(R.id.exitRoutes)

        // TODO: Tirar botão temporario
        showError.setOnClickListener{
            val snack = Snackbar.make(showError, "Mostrando erro", Snackbar.LENGTH_LONG)
            snack.show()
            cardError.visibility = View.VISIBLE
        }

        exitError.setOnClickListener{
            val snack = Snackbar.make(exitError, "Fechando erro", Snackbar.LENGTH_LONG)
            snack.show()
            cardError.visibility = View.GONE
        }

        cardRouteOfTheDay.setOnClickListener{
            val snack = Snackbar.make(cardRouteOfTheDay, "Abrindo card", Snackbar.LENGTH_LONG)
            snack.show()
            cardRoutes.visibility = View.VISIBLE
        }

        exitRoutes.setOnClickListener{
            val snack = Snackbar.make(exitRoutes, "Fechando routes", Snackbar.LENGTH_LONG)
            snack.show()
            cardRoutes.visibility = View.GONE
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true


        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setUpMap()


        val m1 = LatLng(-22.832, -47.005)
        val m2 = LatLng(-22.835, -47.054)
        val m3 = LatLng(-22.835, -47.054)
        val m4 = LatLng(-22.832, -47.054)
        val m5 = LatLng(0.0, 0.0)


        // Add a marker in PUCC and move the camera
        val zoomArea = LatLng(-22.8336937,-47.0546603)
        val cameraBounds = LatLngBounds(
            LatLng(-22.835030, -47.054807),
            LatLng(-22.832038, -47.054493)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomArea, 17.5f))
        mMap.setLatLngBoundsForCameraTarget(cameraBounds)

        val areaAzul = mMap.addPolygon(PolygonOptions()
            .add(
                LatLng(-22.832114, -47.055322),
                LatLng(-22.832038, -47.054493),
                LatLng(-22.835419, -47.053916),
                LatLng(-22.835030, -47.054807))
            .strokeWidth(4.0f)
            .fillColor(Color.argb(30, 0, 0, 240)))


    }


    @SuppressLint("MissingPermission")
    private fun isInsideBounds() {
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val cameraBounds = LatLngBounds(
                    LatLng(-22.835030, -47.054807),
                    LatLng(-22.832038, -47.054493)
                )

                val currentLatLng = LatLng(location.latitude, location.longitude)
                val isInside = cameraBounds.contains(currentLatLng)

                if (isInside) {
                    cardError.visibility = View.VISIBLE
                }
            }
        }
    }

}