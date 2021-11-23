package com.marcosk.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.google.firebase.database.*
import com.marcosk.mapa.Fragment_add_menu
import com.marcosk.mapa.Model.PlaceModel
import java.util.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

//    private val places = arrayListOf(
//        PlaceModel("Regular", "Crime Regular", "regular", LatLng(-23.5868031, -46.6843406)),
//        PlaceModel("leve", "Crime leve", "light", LatLng(-23.5899619, -46.66747)),
//        PlaceModel("Pesadão", "Crime Grave", "severe", LatLng(0.0, 0.0))
//    )

    private val places: ArrayList<PlaceModel?> = ArrayList<PlaceModel?>()

    private lateinit var placeData: DatabaseReference

    private var markerOnMap:Boolean = false
    lateinit var marker: Marker
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var add_marker_button: FloatingActionButton
    private lateinit var fragment_add_menu: Fragment_add_menu

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.getMapAsync{ googleMap ->
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
            fetchData()
            addMarkers(googleMap)
        }



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        add_marker_button = findViewById(R.id.add_marker)
        fragment_add_menu = Fragment_add_menu()

        add_marker_button.hide()

        add_marker_button.setOnClickListener{
            setFragment(fragment_add_menu)
        }
    }

    // * Adds input Fragment to fragmentContainerView inside activity_maps.xlm
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // * Removes input Fragment of fragmentContainerView inside activity_maps.xlm
    fun emptyFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit()
    }

    //Quando o mapa estiver carregado ele executa essa função
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Ativa os botões de zoom do Google
        //mMap.uiSettings.isZoomControlsEnabled = true

        mMap.uiSettings.isMapToolbarEnabled = false

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


    //função quando e colocado um markador na localização clicada no mapa
    private fun placeMarkerOnMap(temporaryMarker: LatLng) {
        val markerOptions = MarkerOptions().position(temporaryMarker)
        if (!this.markerOnMap) {
            this.marker = mMap.addMarker(markerOptions)!!
            this.markerOnMap = true
        }else{
            removeMarkerOnMap(this.marker)
            this.marker = mMap.addMarker(markerOptions)!!
        }
    }

    fun removeMarkerOnMap(marker: Marker){
        marker.remove()
    }

    override fun onMarkerClick(p0: Marker): Boolean {
//        Toast.makeText(this,"Ola",Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMapClick(temporaryMarker: LatLng) {
        placeMarkerOnMap(temporaryMarker)
        add_marker_button.show()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temporaryMarker,15f))
    }

    //Adiciona os marcadores ja existentes no mapa
//    private fun addMarkers(googleMap: GoogleMap) {
//        places.forEach{ place ->
//            if (place.type == "light"){
//                val fixedMarker = googleMap.addMarker(
//                    MarkerOptions()
//                        .position(place.latLang)
//                        .title(place.title)
//                        .snippet(place.desc)
//                        .icon(
//                            BitmapHelper.vectorToBitmap(this, R.drawable.light_crime)
//                        )
//                )
//                fixedMarker.tag = place
//            }else if (place.type == "regular"){
//                val fixedMarker = googleMap.addMarker(
//                    MarkerOptions()
//                        .position(place.latLang)
//                        .title(place.title)
//                        .snippet(place.desc)
//                        .icon(
//                            BitmapHelper.vectorToBitmap(this, R.drawable.regular_crime))
//                )
//                fixedMarker.tag = place
//            }else if (place.type == "severe"){
//                val fixedMarker = googleMap.addMarker(
//                    MarkerOptions()
//                        .position(place.latLang)
//                        .title(place.title)
//                        .snippet(place.desc)
//                        .icon(
//                            BitmapHelper.vectorToBitmap(this, R.drawable.severe_crime))
//                )
//                fixedMarker.tag = place
//            }
//
//        }
//    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            if (place != null) {
                if (place.type == "light") {
                    val fixedMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.longitute, place.latitude))
                            .title(place.title)
                            .snippet(place.desc)
                            .icon(
                                BitmapHelper.vectorToBitmap(this, R.drawable.light_crime)
                            )
                    )
                    fixedMarker?.tag = place
                } else if (place.type == "regular") {
                    val fixedMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.longitute, place.latitude))
                            .title(place.title)
                            .snippet(place.desc)
                            .icon(
                                BitmapHelper.vectorToBitmap(this, R.drawable.regular_crime)
                            )
                    )
                    fixedMarker?.tag = place
                } else if (place.type == "severe") {
                    val fixedMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.longitute, place.latitude))
                            .title(place.title)
                            .snippet(place.desc)
                            .icon(
                                BitmapHelper.vectorToBitmap(this, R.drawable.severe_crime)
                            )
                    )
                    fixedMarker?.tag = place
                }
            }
        }
    }

    fun fetchData(){
        placeData = FirebaseDatabase.getInstance().getReference("places")

        placeData.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (placesSnapshot in dataSnapshot.children) {
                    val place: PlaceModel? = placesSnapshot.getValue(PlaceModel::class.java)

                    places.add(place)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }

        })
    }
}
