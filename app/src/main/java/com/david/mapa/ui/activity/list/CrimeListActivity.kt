package com.david.mapa.ui.activity.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.mapa.R
import com.david.mapa.model.PlaceModel
import com.david.mapa.ui.activity.adapter.CrimeListAdapter
import com.google.firebase.database.*

class CrimeListActivity : AppCompatActivity() {

    private lateinit var _list: List<PlaceModel>
    private val crimeListAdapter by lazy { CrimeListAdapter(this, _list) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_list)
        getData()
    }

    private fun getData() {
        val placeData = FirebaseDatabase.getInstance().getReference("Place")
        val places: ArrayList<PlaceModel> = arrayListOf()
        placeData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {

                        val place = data.getValue(PlaceModel::class.java)
                        if (place != null) {
                            places.add(place)
                        }
                    }
                }
                _list = places
                setupRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupRecyclerView() {
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = crimeListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}