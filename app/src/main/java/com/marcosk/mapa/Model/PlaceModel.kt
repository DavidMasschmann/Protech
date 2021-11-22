package com.marcosk.mapa.Model
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase

data class PlaceModel (
    val title: String,
    val desc: String,
    val type: String,
    val latLang: LatLng
){

//    var id: String = ""

    fun salvar(){
        var reference = FirebaseDatabase.getInstance().reference
        reference.child("places").child(title).setValue(this)
    }
}