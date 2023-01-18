package com.example.guru16application.ui.shelter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShelterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is shelter Fragment"
    }
    val text: LiveData<String> = _text
}