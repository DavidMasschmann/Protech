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

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_add_menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_add_menu : Fragment(), View.OnClickListener {

    private lateinit var add_crime_button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



//        val view: View = inflater.inflate(R.layout.floating_add_menu, container, false)

//        view.setOnClickListener(){
//
//            Toast.makeText(context, "HAHA", Toast.LENGTH_SHORT).show()
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_menu, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_crime_button = view.findViewById(R.id.add_crime_button)

        add_crime_button.setOnClickListener(this)
    }

    private fun doSomething(){
        Toast.makeText(context, "fun", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        doSomething()
    }

}