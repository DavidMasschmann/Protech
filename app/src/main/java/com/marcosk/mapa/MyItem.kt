package com.marcosk.mapa

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyItem(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)
    private val title: String = title
    private val snippet: String = snippet

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

}
