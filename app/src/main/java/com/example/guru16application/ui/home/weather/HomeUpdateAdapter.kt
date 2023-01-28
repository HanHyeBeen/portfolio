package com.example.guru16application.ui.home.weather

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.R
import com.example.guru16application.databinding.FragmentHomeWeatherItemsBinding

class HomeUpdateAdapter (private val dataSet : Array<String>) : RecyclerView.Adapter<HomeUpdateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home_weather_items, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.datetextView.text = dataSet[position]
        holder.temperaturetextView.text = dataSet[position]
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val datetextView : TextView
        val temperaturetextView : TextView

        init{
            datetextView = view.findViewById(R.id.dateTextView)
            temperaturetextView = view.findViewById(R.id.temperatureTextView)
        }
    }
}