package com.example.guru16application.ui.home.api

import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherAPI: WeatherAPI
){
    suspend fun getWeather(
        dateType : String, numOfRows : Int, pageNo : Int, baseDate : Int, baseTime : Int, nx : String, ny : String
    ) : Response<Weather> {
        return weatherAPI.getWeather(dateType,numOfRows,pageNo,baseDate, baseTime,nx,ny)
    }
}