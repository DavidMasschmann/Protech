package com.marcosk.mapa.Model
import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase

class PlaceModel() {

    var title: String = ""
    var type: String = ""
    var desc: String = ""
    var latitude: Double = 0.0
    var longitute: Double = 0.0

    constructor(title: String, type: String, desc: String, latitute: Double, longitude: Double) : this() {}



    fun salvar(){

        var reference = FirebaseDatabase.getInstance().reference
        reference.child("places").child(title).setValue(this)

    }

}