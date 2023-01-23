package com.example.guru16application.ui.clothing

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.example.guru16application.R

class ReViewAdapter (val context: Context, val Relist: ArrayList<ReViewItem>): RecyclerView.Adapter<ReViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!){
        var clothImg = view?.findViewById<ImageView>(R.id.imgView_item)
        var clothName = view?.findViewById<TextView>(R.id.txt_main)
        var Review = view?.findViewById<RelativeLayout>(R.id.firstLinear)

        fun bind(room:ReViewItem, context: Context, onClickListener: View.OnClickListener){

            clothImg?.setImageBitmap(room.image)
            clothName?.text = room.name
            Review?.setOnClickListener(onClickListener)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.clothing_list,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val citem = Relist[position]

        holder.apply {
            bind(citem, context, View.OnClickListener {
                Toast.makeText(context, "${citem.name}", Toast.LENGTH_SHORT).show()
                //이동할 intent
                var intent: Intent = Intent(context,ClothNextActivity::class.java)
                intent.putExtra("searchName", citem.name)
                context.startActivity(intent)
            })
        }


    }

    interface OnItemClickListener{
        fun onClick(v:View, position: Int)
    }


    fun setItemClickListener(itemClickListener: OnItemClickListener){
        this.itemClickListener = itemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount(): Int = Relist.size
}