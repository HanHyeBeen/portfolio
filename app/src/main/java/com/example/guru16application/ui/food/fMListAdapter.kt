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

class fMListAdapter(val context: Context, val list: ArrayList<fMListItem>): BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): fMListItem = list[position]

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.foodmenu_list, null)

        var fMenu : TextView = view.findViewById(R.id.fmenu_list)
        var fPri : TextView = view.findViewById(R.id.fprice_list)


        val items = list[position]

        fMenu.text = items.menu
        fPri.text = items.price


        return view

    }
}