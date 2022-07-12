package com.david.mapa.model
import com.google.firebase.database.FirebaseDatabase

data class PlaceModel(

    var title: String,
    var desc: String,
    var type: String,
    var lat: Double,
    var long: Double

){

    constructor (): this(
        "",
        "",
        "",
        0.0,
        0.0
    )

    fun save(){

        val idPlace: Int = System.currentTimeMillis().toInt()
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("Place").child(idPlace.toString()).setValue(this)

    }

}
