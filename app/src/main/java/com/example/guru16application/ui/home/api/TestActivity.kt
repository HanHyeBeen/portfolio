package com.example.guru16application.ui.home.api

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import com.example.guru16application.R
import com.example.guru16application.databinding.ActivityTestBinding
import com.example.guru16application.ui.home.api.Constants.Companion.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private val viewModel : WeatherViewModel by viewModels()

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_test)
        binding.lifecycleOwner = this

        viewModel.getWeather("JSON", 8, 1, "20230126", "0800", 62, 128)

        viewModel.weatherResponse.observe(this) {
            for (i in it.body()?.response!!.body.items.item) {
                Log.d(TAG, "$i")
            }
        }
    }
}