package com.david.mapa.ui.activity.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.mapa.R
import com.david.mapa.model.PlaceModel
import com.david.mapa.ui.activity.MapsActivity
import com.david.mapa.ui.activity.adapter.CrimeListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CrimeListActivity : AppCompatActivity() {

    lateinit var myAdapter: CrimeListAdapter
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_list)
        user = FirebaseAuth.getInstance()
        getData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private fun clickAdapter() {
        myAdapter.setOnclickListener { placeModel ->
            FirebaseDatabase.getInstance().getReference("Place").child(placeModel.id.toString()).removeValue()
        }
    }

    private fun getData() {
        val placeData = FirebaseDatabase.getInstance().getReference("Place")
        placeData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var places: ArrayList<PlaceModel> = arrayListOf()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {

                        val place = data.getValue(PlaceModel::class.java)
                        if (place != null) {
                            places.add(place)
                        }
                    }
                }

                places = places.filter { it.userID.equals(user.currentUser?.uid.toString()) } as ArrayList<PlaceModel>
                setupRecyclerView(places)
                clickAdapter()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupRecyclerView(places: ArrayList<PlaceModel>) {
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = CrimeListAdapter(this.context, places)
            myAdapter = adapter as CrimeListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}