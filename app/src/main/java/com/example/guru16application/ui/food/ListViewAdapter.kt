package com.example.guru16application.ui.food

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.guru16application.R
import com.example.guru16application.ui.food.ListViewItem

class ListViewAdapter(val context: Context, val list: ArrayList<ListViewItem>): BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): ListViewItem = list[position]

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.food_list, null)

        var fImage :ImageView = view!!.findViewById(R.id.foodImage)
        var fName : TextView = view.findViewById(R.id.foodName)
        var fLoc : TextView = view.findViewById(R.id.foodLoc)
        var fTime : TextView = view.findViewById(R.id.foodTime)

        val items = list[position]

        fImage.setImageBitmap(items.image)
        fName.text = items.name
        fLoc.text = items.loc
        fTime.text = items.time

        return view

    }
}