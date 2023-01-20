package com.example.guru16application.ui.clothing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.guru16application.R

class GriViewAdapter(val context: Context, val itemlist: ArrayList<ReViewItem>) : BaseAdapter() {

    override fun getCount(): Int = itemlist.size

    override fun getItem(position: Int): ReViewItem = itemlist[position]

    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.clothing_list, null)

        var clothimage : ImageView = view!!.findViewById(R.id.imgView_item)
        var clothname : TextView = view.findViewById(R.id.txt_main)

        //var convertView = view

        // if (convertView == null) {
        //    convertView = LayoutInflater.from(parent?.context).inflate(R.layout.costm_list, parent, false)
        //}
        val items = itemlist[position]

        //var item: ListViewItem = items[position]
        clothimage.setImageBitmap(items.image)
        clothname.text = items.name


        return view

    }

}