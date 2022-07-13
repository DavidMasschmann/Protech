package com.david.mapa.repository

import com.david.mapa.model.PlaceModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val reference: DatabaseReference
){

    fun setPlace(placeModel: PlaceModel){
        reference.child("Place").child(placeModel.id.toString()).setValue(placeModel)
    }

    fun deletePlace(placeModel: PlaceModel){
        reference.child("Place").child(placeModel.id.toString()).removeValue()
    }

}