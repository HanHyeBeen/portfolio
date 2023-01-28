package com.example.guru16application.ui.home.weather

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.R
import com.example.guru16application.databinding.FragmentHomeWeatherItemsBinding


//class HomeUpdateViewHolder(val binding : FragmentHomeWeatherItemsBinding) : RecyclerView.ViewHolder(binding.root)
//
//class HomeUpdateAdapter (val binding: FragmentHomeWeatherItemsBinding) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    var items = ArrayList<HomeUpdate>()
//    // val listener : OnClickListener
//    var layoutType : Int = 0
//
//    override fun getItemCount(): Int = items.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home_weather_items, parent, false)
//        return HomeUpdateViewHolder(binding.view)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val binding = (holder as HomeUpdateViewHolder).binding
//        binding.dateTextView.text = items[position]
//        binding.homeWeatherLayout1.setOnClickListener{
//
//        }
//    }
//}