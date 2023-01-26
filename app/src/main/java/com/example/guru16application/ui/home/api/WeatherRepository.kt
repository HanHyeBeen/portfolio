package com.example.guru16application.ui.home.api

import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherAPI: WeatherAPI
){
    suspend fun getWeather(
        dataType : String, numOfRows : Int, pageNo : Int,
        baseDate : String, baseTime : String, nx : Int, ny : Int
    ) : Response<Weather> {
        return weatherAPI.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny)
    }
}