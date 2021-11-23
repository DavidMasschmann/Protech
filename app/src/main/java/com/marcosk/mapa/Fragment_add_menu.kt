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
    lateinit var add_crime_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_add_menu, container, false)

        add_crime_button = view.findViewById(R.id.add_crime_button)

        val crime_name = view.findViewById(R.id.crime_name) as EditText
        val crime_description = view.findViewById(R.id.crime_description) as EditText
        val crime_type_group = view.findViewById(R.id.crime_type) as RadioGroup
        var crime_type : RadioButton



// *      Botões antigos de imagem, não vão ser usados agora
//        val light_crime_button = view.findViewById(R.id.light_crime_button) as ImageButton
//        val regular_crime_button = view.findViewById(R.id.regular_crime_button) as ImageButton
//        val severe_crime_button = view.findViewById(R.id.severe_crime_button) as ImageButton



//        add_crime_button.setOnClickListener { view ->
//            MapsActivity
//            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
//        }

        add_crime_button.setOnClickListener {
// *          Teste input e output de dados no crime_name
//            if (crime_name.text.toString() != "")
//                Toast.makeText(context, crime_name.text, Toast.LENGTH_SHORT).show()

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
            activityVariable.add_marker_button.hide()
        }

        return view

// *    This does work, but the click is anywhere in the view
//        val view: View = inflater.inflate(R.layout.fragment_add_menu, container, false)
//
//        view.setOnClickListener(){
//            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
//        }
//
////        Inflate the layout for this fragment
//        return view


// *     One of my tests that didn't work
//        val bind = FragmentAddMenuBinding.inflate(layoutInflater)
//
//        Toast.makeText(context, "ANTES", Toast.LENGTH_SHORT).show()
//
//        bind.addCrimeButton.setOnClickListener {
//            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
//        }
//
//        return bind.root

// *    Original return
//      return inflater.inflate(R.layout.fragment_add_menu, container, false)
    }

// *  Função para retornar as infos para o MapsActivity, não funciona, ainda...
//    fun returnInfo(crime_name: EditText, crime_description: EditText, type: String): Place {
//        return Place(
//            crime_name.text.toString(),
//            crime_description.text.toString(),
//            type,
//            LatLng(0.0, 0.0)
//        )
//    }

}
