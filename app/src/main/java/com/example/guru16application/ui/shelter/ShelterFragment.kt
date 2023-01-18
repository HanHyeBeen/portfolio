package com.example.guru16application.ui.shelter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.guru16application.databinding.FragmentHomeBinding
import com.example.guru16application.databinding.FragmentShelterBinding

class ShelterFragment : Fragment() {

    // ──────────────────────────────────────────────────────────── 프래그먼트 초기화
    private var _binding: FragmentShelterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shelterViewModel =
            ViewModelProvider(this).get(ShelterViewModel::class.java)

        _binding = FragmentShelterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textShelter
        shelterViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ──────────────────────────────────────────────────────────── 프래그먼트 초기화 끝
}