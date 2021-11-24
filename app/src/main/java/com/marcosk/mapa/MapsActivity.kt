package com.marcosk.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
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
import com.marcosk.mapa.Model.PlaceModel
import java.util.ArrayList
import com.google.firebase.database.DataSnapshot





class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private lateinit var placeData: DatabaseReference
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var markerOnMap: Boolean = false
    private lateinit var fragmentAddMenu: Fragment_add_menu

    lateinit var addMarkerButton: FloatingActionButton
    lateinit var marker: Marker

    companion object {

        private const val LOCATION_REQUEST_CODE = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        mapFragment.getMapAsync { googleMap ->

            getData(googleMap)
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))


        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fragmentAddMenu = Fragment_add_menu()

        addMarkerButton = findViewById(R.id.add_marker)
        addMarkerButton.hide()
        addMarkerButton.setOnClickListener {

            setFragment(fragmentAddMenu)

        }
    }

    // * Adds input Fragment to fragmentContainerView inside activity_maps.xlm
    private fun setFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {

            replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit()

        }
    }

    // * Removes input Fragment of fragmentContainerView inside activity_maps.xlm
    fun emptyFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit()

    }

    //Quando o mapa estiver carregado ele executa essa função
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)

        setUpMap()

    }

    //função que verifica as permissões do app
    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return

        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

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

        } else {

            removeMarkerOnMap(this.marker)
            this.marker = mMap.addMarker(markerOptions)!!

        }
    }

    fun removeMarkerOnMap(marker: Marker) {

        marker.remove()

    }

    override fun onMarkerClick(p0: Marker): Boolean {

        return false

    }

    override fun onMapClick(temporaryMarker: LatLng) {

        placeMarkerOnMap(temporaryMarker)
        addMarkerButton.show()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temporaryMarker, 15f))

    }

    private fun addMarkers(googleMap: GoogleMap, places: ArrayList<PlaceModel>) {

        places.forEach { place ->

            if (place != null) {

                when (place.type) {
                    "Light" -> {
                        val fixedMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(place.lat, place.long))
                                .title(place.title)
                                .snippet(place.desc)
                                .icon(
                                    BitmapHelper.vectorToBitmap(this, R.drawable.light_crime)
                                )
                        )
                        fixedMarker?.tag = place
                    }
                    "Regular" -> {
                        val fixedMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(place.lat, place.long))
                                .title(place.title)
                                .snippet(place.desc)
                                .icon(
                                    BitmapHelper.vectorToBitmap(this, R.drawable.regular_crime)
                                )
                        )
                        fixedMarker?.tag = place
                    }
                    "Severe" -> {

                        val fixedMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(place.lat, place.long))
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
    }

        fun getData(googleMap: GoogleMap) {

            placeData = FirebaseDatabase.getInstance().getReference("Place")

            placeData.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val places: ArrayList<PlaceModel> = arrayListOf<PlaceModel>()

                    if (snapshot.exists()) {

                        for (data in snapshot.children) {

                            val place = data.getValue(PlaceModel::class.java)
                            if (place != null) {
                                places.add(place)
                            }
                        }

                        addMarkers(googleMap, places)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }