package com.david.mapa.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.david.mapa.R
import com.david.mapa.model.PlaceModel
import com.david.mapa.ui.activity.fragments.FragmentAddMenu
import com.david.mapa.ui.activity.login.LoginOrRegisterActivity
import com.david.mapa.ui.adapter.MarkerInfoAdapter
import com.david.mapa.utils.BitmapHelper
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    private lateinit var placeData: DatabaseReference
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var fragmentAddMenu: FragmentAddMenu
    private var markerOnMap: Boolean = false
    lateinit var addMarkerButton: FloatingActionButton
    lateinit var marker: Marker
    private lateinit var user: FirebaseAuth

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setupMap()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        configMarkerButton()
        setupSearchAutocomplete()
        setupLogoutBtn()
    }

    //When maps activity is inflated call this:
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)
        getPermissions()
        setupMyLocationBtn()
    }

    private fun setupMyLocationBtn() {
        var currentLatLong: LatLng? = null

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLong = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong!!, 15f))
            }
        }

        val gpsBtn = findViewById<FloatingActionButton>(R.id.gps_button)
        gpsBtn.setOnClickListener(){
            val cameraUpdate = currentLatLong?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
            if (cameraUpdate != null) {
                mMap.animateCamera(cameraUpdate)
            }
        }
    }

    private fun setupLogoutBtn() {
        user = FirebaseAuth.getInstance()
        val logoutBtn = findViewById<FloatingActionButton>(R.id.logout_button)
        logoutBtn.setOnClickListener() {
            user.signOut()
            startActivity(Intent(this, LoginOrRegisterActivity::class.java))
        }
    }

    private fun setupSearchAutocomplete() {
        // Initializing the Places API
        // with the help of our API_KEY
        val apiKey = getApiKey()

        Places.initialize(applicationContext, apiKey)

        // Initialize Autocomplete Fragments
        // from the main activity layout file
        val autocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        // Information that we wish to fetch after typing
        // the location and clicking on one of the options
        autocompleteSupportFragment!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL
            )
        )

        // Display the fetched information after clicking on one of the options
        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng!!, 15f))
            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getApiKey(): String {
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        return ai.metaData["com.google.android.geo.API_KEY"].toString()
    }

    private fun configMarkerButton() {
        fragmentAddMenu = FragmentAddMenu()
        addMarkerButton = findViewById(R.id.add_marker)
        addMarkerButton.hide()
        addMarkerButton.setOnClickListener {
            setFragment(fragmentAddMenu)
        }
    }

    //Inflate the maps activity and get the sync
    @SuppressLint("PotentialBehaviorOverride")
    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync {
            getData(it)
            it.setInfoWindowAdapter(MarkerInfoAdapter(this))
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

        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    //Check app permissions
    private fun getPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }

    //Place a temporary marker when the user clicks on the map
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

    //Show add marker button and center camera on click
    override fun onMapClick(temporaryMarker: LatLng) {
        placeMarkerOnMap(temporaryMarker)
        addMarkerButton.show()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temporaryMarker, 15f))
    }

    //Function that filters the marks on the map
    private fun markersFilter(googleMap: GoogleMap, places: ArrayList<PlaceModel>) {
        places.forEach { place ->
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

    //Get data from database
    private fun getData(googleMap: GoogleMap) {
        placeData = FirebaseDatabase.getInstance().getReference("Place")
        placeData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val places: ArrayList<PlaceModel> = arrayListOf()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {

                        val place = data.getValue(PlaceModel::class.java)
                        if (place != null) {
                            places.add(place)
                        }
                    }
                    markersFilter(googleMap, places)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}