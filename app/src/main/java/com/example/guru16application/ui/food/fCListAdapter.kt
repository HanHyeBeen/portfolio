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

class fCListAdapter(val context: Context, val list: ArrayList<String>): BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): String = list[position]

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.foodment_list, null)

        var fCom : TextView = view.findViewById(R.id.fcom)


        val items = list[position]

        fCom.text = items


        return view

    }
}