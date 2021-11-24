package com.marcosk.mapa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.marcosk.mapa.Model.PlaceModel


/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_add_menu.newInstance] factory method to
 * create an instance of this fragment.
 */

class Fragment_add_menu : Fragment() {

    lateinit var place : PlaceModel
    lateinit var addCrimeButton: Button

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_add_menu, container, false)

        addCrimeButton = view.findViewById(R.id.add_crime_button)

        val crime_name = view.findViewById(R.id.crime_name) as EditText
        val crime_description = view.findViewById(R.id.crime_description) as EditText
        val crime_type_group = view.findViewById(R.id.crime_type) as RadioGroup
        var crime_type : RadioButton

        addCrimeButton.setOnClickListener {

            val selectedId: Int = crime_type_group.getCheckedRadioButtonId()
            crime_type = view.findViewById(selectedId)

            val activityVariable: MapsActivity = activity as MapsActivity

            place = PlaceModel(
                crime_name.text.toString(),
                crime_description.text.toString(),
                crime_type.text.toString(),
                activityVariable.marker.position.latitude,
                activityVariable.marker.position.longitude
            )

            activityVariable.emptyFragment(this)

            place.salvar()

            crime_name.text.clear()
            crime_description.text.clear()
            crime_type_group.clearCheck()

            activityVariable.removeMarkerOnMap(activityVariable.marker)
            activityVariable.addMarkerButton.hide()



        }

        return view

    }

}
