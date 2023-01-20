package com.example.guru16application.ui.clothing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.example.guru16application.R

class ReViewAdapter (val context: Context, val Relist: ArrayList<ReViewItem>): RecyclerView.Adapter<ReViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!){
        var clothImg = view?.findViewById<ImageView>(R.id.imgView_item)
        var clothName = view?.findViewById<TextView>(R.id.txt_main)

        fun bind(room:ReViewItem, context: Context){

            clothImg?.setImageBitmap(room.image)
            clothName?.text = room.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.clothing_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Relist[position], context)
    }

    override fun getItemCount(): Int = Relist.size
}