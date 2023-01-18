package com.example.guru16application.ui.clothing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.guru16application.databinding.FragmentClothingBinding


class ClothingFragment : Fragment() {

    private var _binding: FragmentClothingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clothingViewModel =
            ViewModelProvider(this).get(ClothingViewModel::class.java)

        _binding = FragmentClothingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textClothing
        clothingViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}