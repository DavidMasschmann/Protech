package com.marcosk.mapa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoAdapter (private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View? {

        val place = marker.tag as? Place ?: return null

        val view = LayoutInflater.from(context).inflate(R.layout.custom_marker_info, null)

        view.findViewById<TextView>(R.id.txt_title).text = place.title
        view.findViewById<TextView>(R.id.txt_desc).text = place.desc
        view.findViewById<TextView>(R.id.txt_type).text = place.type

        return view
    }
}