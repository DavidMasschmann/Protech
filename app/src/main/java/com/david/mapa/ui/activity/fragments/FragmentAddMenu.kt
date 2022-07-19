package com.david.mapa.ui.activity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.david.mapa.model.PlaceModel
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAddMenu.newInstance] factory method to
 * create an instance of this fragment.
 */


class FragmentAddMenu : Fragment(){
    private lateinit var user: FirebaseAuth
    private lateinit var place: PlaceModel
    lateinit var addCrimeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_menu, container, false)

        addCrimeButton = view.findViewById(R.id.add_crime_button)

        val crimeName = view.findViewById(R.id.crime_name) as EditText
        val crimeDescription = view.findViewById(R.id.crime_description) as EditText
        val crimeTypeGroup = view.findViewById(R.id.crime_type) as RadioGroup
        var crimeType: RadioButton
        user = FirebaseAuth.getInstance()

        addCrimeButton.setOnClickListener {

            val selectedId: Int = crimeTypeGroup.checkedRadioButtonId
            crimeType = view.findViewById(selectedId)

            val activityVariable: MapsActivity = activity as MapsActivity

            place = PlaceModel(
                System.currentTimeMillis().toInt(),
                crimeName.text.toString(),
                crimeDescription.text.toString(),
                crimeType.text.toString(),
                user.currentUser?.uid.toString(),
                Date(),
                activityVariable.marker.position.latitude,
                activityVariable.marker.position.longitude
            )

            activityVariable.emptyFragment(this)

            FirebaseDatabase.getInstance().getReference("Place").child(place.id.toString()).setValue(place)

            crimeName.text.clear()
            crimeDescription.text.clear()
            crimeTypeGroup.clearCheck()

            activityVariable.marker.remove()
            activityVariable.addMarkerButton.hide()
        }

        return view
    }
}
