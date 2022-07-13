package com.david.mapa.model

data class PlaceModel(
    val id: Int? = null,
    var title: String? = null,
    var desc: String? = null,
    var type: String? = null,
    var lat: Double? = null,
    var long: Double? = null
)
