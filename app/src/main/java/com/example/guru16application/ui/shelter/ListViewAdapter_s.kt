package com.example.guru16application.ui.shelter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.guru16application.R
import com.example.guru16application.ui.food.ListViewItem

class ListViewAdapter_s(val context: Context, val list: ArrayList<ListViewItem_s>): BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): ListViewItem_s = list[position]

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.shelter_list, null)

        var building : TextView = view.findViewById(R.id.building)
        var classroom : TextView = view.findViewById(R.id.classroom)

        val items = list[position]

        building.text = items.building
        classroom.text = items.classroom

        return view

    }
}