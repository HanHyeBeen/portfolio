package com.example.guru16application.ui.home.api

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guru16application.R

class WeatherAdapter (var items : Array<ModelWeather>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home_weather_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int = items.count()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetText|18n")
        fun setItem(item : ModelWeather) {
            val imgWeather = itemView.findViewById<ImageView>(R.id.imgWeather)
            val tvTemp = itemView.findViewById<TextView>(R.id.temperatureTextView)

            imgWeather.setImageResource(getRainImage(item.rainType, item.sky))
            tvTemp.text = item.temp + "℃"
        }
    }

    fun getRainImage(rainType : String, sky : String) : Int {
        return when(rainType) {
            "0" -> getWeatherImage(sky) // 강수없음
            "1" -> R.drawable.ic_baseline_rain // 비
            "2" -> R.drawable.ic_baseline_snowrain // 비눈
            "3" -> R.drawable.ic_baseline_snow // 눈
            "5" -> R.drawable.ic_baseline_rain // 빗방울
            "6" -> R.drawable.ic_baseline_snowrain // 비방울 눈날림
            "7" -> R.drawable.ic_baseline_snow// 눈날림
            else -> R.drawable.ic_baseline_question_mark_24 // 오류 시
        }
    }

    private fun getWeatherImage(sky : String) : Int {
        return when(sky) {
            "1" -> R.drawable.ic_baseline_wb_sunny // 맑음
            "3" -> R.drawable.ic_baseline_cloud // 구름 많음
            "4" -> R.drawable.ic_baseline_fog // 흐림
            else -> R.drawable.ic_baseline_question_mark_24 // 오류 시
        }
    }
}