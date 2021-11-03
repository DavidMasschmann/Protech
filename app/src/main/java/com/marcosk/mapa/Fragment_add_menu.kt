package com.marcosk.mapa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marcosk.mapa.databinding.FragmentAddMenuBinding
import com.google.android.material.button.MaterialButton

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_add_menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_add_menu : Fragment() {

    private lateinit var add_crime_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// *      One of my tests that didn't work

        val view: View = inflater.inflate(R.layout.fragment_add_menu, container, false)

        add_crime_button = view.findViewById(R.id.add_crime_button)

        add_crime_button.setOnClickListener { view ->
            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
        }

        return view

// *    One of my tests that didn't work
//        val view: View = inflater.inflate(R.layout.floating_add_menu, container, false)
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
////            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
//        }

//        return bind.root

// *    Original return
//      return inflater.inflate(R.layout.fragment_add_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        add_crime_button = view.findViewById(R.id.add_crime_button)
//
//        add_crime_button.setOnClickListener(this)
    }

//    override fun onClick(p0: View?) {
//        Toast.makeText(context, "ON CLICK", Toast.LENGTH_SHORT).show()
//    }

//    override fun onClick(p0: View?) {
//        Toast.makeText(context, "fun", Toast.LENGTH_SHORT).show()
//    }

}