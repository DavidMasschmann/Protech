package com.marcosk.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private val places = arrayListOf(
        Place("Regular", "Crime Regular", "regular", LatLng(-23.5868031, -46.6843406)),
        Place("leve", "Crime leve", "light", LatLng(-23.5899619, -46.66747))
    )

    private var markerOnMap:Boolean = false
    private lateinit var marker: Marker
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var add_marker_button: FloatingActionButton
    private lateinit var fragment_floating_add_menu: Floating_add_menu

    private lateinit var test_button: FloatingActionButton

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (this)
        mapFragment.getMapAsync{ googleMap ->
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
            addMarkers(googleMap)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        add_marker_button = findViewById(R.id.add_marker)
        fragment_floating_add_menu = Floating_add_menu()

        add_marker_button.setOnClickListener{
            setFragment(fragment_floating_add_menu)
        }

        test_button = findViewById(R.id.notification_button)

        test_button.setOnClickListener{
            emptyFragment(fragment_floating_add_menu)

        }
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun emptyFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit()
    }

    //Quando o mapa estiver carregado ele executa essa função
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Ativa os botões de zoom do Google
        //mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapClickListener (this)
        setUpMap()
        mMap.setOnMarkerClickListener(this)
    }

    //função que verifica as permissões do app
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener (this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }

    //Adiciona os marcadores ja existentes no mapa
    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach{ place ->
            if (place.type == "light"){
                val fixedMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place.latLang)
                        .title(place.title)
                        .snippet(place.desc)
                        .icon(
                            BitmapHelper.vectorToBitmap(this, R.drawable.light_icon)
                        )
                )
                fixedMarker.tag = place
            }else if (place.type == "regular"){
                val fixedMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place.latLang)
                        .title(place.title)
                        .snippet(place.desc)
                        .icon(
                            BitmapHelper.vectorToBitmap(this, R.drawable.regular_icon))
                )
                fixedMarker.tag = place
            }else if (place.type == "severe"){
                val fixedMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place.latLang)
                        .title(place.title)
                        .snippet(place.desc)
                        .icon(
                            BitmapHelper.vectorToBitmap(this, R.drawable.severe_icon))
                )
                fixedMarker.tag = place
            }

        }
    }

    //função quando e colocado um markador na localização clicada no mapa
    private fun placeMarkerOnMap(temporaryMarker: LatLng) {
        val markerOptions = MarkerOptions().position(temporaryMarker)
        if (!this.markerOnMap) {
            this.marker = mMap.addMarker(markerOptions)
            this.markerOnMap = true
        }else{
            removeMarkerOnMap(this.marker)
            this.marker = mMap.addMarker(markerOptions)
        }
    }

    private fun removeMarkerOnMap(marker: Marker){
        marker.remove()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(this,"Ola",Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMapClick(temporaryMarker: LatLng) {
        placeMarkerOnMap(temporaryMarker)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temporaryMarker,15f))
    }

}

data class Place(
    val title: String,
    val desc: String,
    val type: String,
    val latLang:LatLng
)
