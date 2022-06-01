package com.david.mapa.ui.activity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.david.mapa.Model.PlaceModel
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAddMenu.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentAddMenu : Fragment() {

    lateinit var place: PlaceModel
    lateinit var addCrimeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_add_menu, container, false)

        addCrimeButton = view.findViewById(R.id.add_crime_button)

        val crimeName = view.findViewById(R.id.crime_name) as EditText
        val crimeDescription = view.findViewById(R.id.crime_description) as EditText
        val crimeTypeGroup = view.findViewById(R.id.crime_type) as RadioGroup
        var crimeType: RadioButton

        addCrimeButton.setOnClickListener {

            val selectedId: Int = crimeTypeGroup.checkedRadioButtonId
            crimeType = view.findViewById(selectedId)

            val activityVariable: MapsActivity = activity as MapsActivity

            place = PlaceModel(
                crimeName.text.toString(),
                crimeDescription.text.toString(),
                crimeType.text.toString(),
                activityVariable.marker.position.latitude,
                activityVariable.marker.position.longitude
            )

            activityVariable.emptyFragment(this)

            place.salvar()

            crimeName.text.clear()
            crimeDescription.text.clear()
            crimeTypeGroup.clearCheck()

            activityVariable.removeMarkerOnMap(activityVariable.marker)
            activityVariable.addMarkerButton.hide()
        }

        return view
    }
}
