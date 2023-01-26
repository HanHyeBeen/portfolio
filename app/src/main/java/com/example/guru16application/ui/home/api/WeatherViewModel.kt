package com.example.guru16application.ui.home.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherResponse :
            MutableLiveData<Response<Weather>> = MutableLiveData()

    val weatherResponse get() = _weatherResponse

    fun getWeather(dataType : String, numOfRows : Int, pageNo : Int,
                   baseDate : String, baseTime : String, nx : Int, ny : Int) {
        viewModelScope.launch {
            val response = repository.getWeather(dataType, numOfRows, pageNo, baseDate, baseTime, nx, ny)
            _weatherResponse.value = response
        }
    }


}