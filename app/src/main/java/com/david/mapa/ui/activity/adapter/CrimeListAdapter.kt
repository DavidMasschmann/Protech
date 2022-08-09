package com.david.mapa.ui.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.mapa.R
import com.david.mapa.databinding.CrimeItemBinding
import com.david.mapa.model.PlaceModel
import com.david.mapa.utils.limitDescription
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CrimeListAdapter(
    private var context: Context,
    private var places: List<PlaceModel>
) : RecyclerView.Adapter<CrimeListAdapter.CrimeViewHolder>() {

    inner class CrimeViewHolder(val binding: CrimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
        return CrimeViewHolder(
            CrimeItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
        val place = places[position]

        holder.binding.apply {
            title.text = place.title
            if (place.desc == "") {
                description.text = holder.itemView.context.getString(R.string.empty_description)
            } else {
                description.text = place.desc?.limitDescription(40)
            }

            val dateFormat : DateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA)
            val data = place.date?.let { dateFormat.format(it) }

            date.text = data

            when (place.type) {
                "Light" -> {
                    crimeIcon.setImageResource(R.drawable.light_crime)
                }
                "Regular" -> {
                    crimeIcon.setImageResource(R.drawable.regular_crime)
                }
                "Severe" -> {
                    crimeIcon.setImageResource(R.drawable.severe_crime)
                }
            }
        }

        holder.binding.deleteBtn.setOnClickListener {
            onDeleteItemClick?.let {
                it(place)
            }
        }
    }

    private var onDeleteItemClick: ((PlaceModel) -> Unit)? = null

    fun setOnclickListener(listener: (PlaceModel) -> Unit) {
        onDeleteItemClick = listener
    }
}
















