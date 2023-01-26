package com.example.guru16application.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.guru16application.databinding.FragmentHomeBinding
import com.example.guru16application.ui.home.api.Constants.Companion.TAG
import com.example.guru16application.ui.home.api.WeatherViewModel

class HomeFragment : Fragment() {

    // ──────────────────────────────────────────────────────────── 프래그먼트 초기화

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // weather view
        //val weatherViewModel by viewModels<WeatherViewModel>()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
//         오류 코드
//        weatherViewModel.getWeather("JSON", 3, 1, 20230126, 0800, "62", "128")
//        weatherViewModel.weatherResponse.observe(viewLifecycleOwner){
//            for(i in it.body()?.response!!.body.items.item){
//                Log.d(TAG, "$i")
//            }
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}