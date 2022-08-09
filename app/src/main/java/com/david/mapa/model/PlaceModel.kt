package com.david.mapa.model

import java.util.*

data class PlaceModel(
    val id: Int? = null,
    var title: String? = null,
    var desc: String? = null,
    var type: String? = null,
    var userID: String? = null,
    var date: Date? = null,
    var lat: Double? = null,
    var long: Double? = null
)