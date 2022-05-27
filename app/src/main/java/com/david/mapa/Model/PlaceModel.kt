package com.david.mapa.Model
import com.google.firebase.database.FirebaseDatabase

data class PlaceModel(

    var title: String,
    var desc: String,
    var type: String,
    var lat: Double,
    var long: Double

){

    constructor (): this("","","", 0.0, 0.0)

    fun salvar(){

        var idPlace: Int = System.currentTimeMillis().toInt()
        var reference = FirebaseDatabase.getInstance().reference
        reference.child("Place").child(idPlace.toString()).setValue(this)

    }

}
