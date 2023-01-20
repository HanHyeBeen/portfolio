package com.example.guru16application.ui.clothing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClothingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is clothing Fragment"
    }
    val text: LiveData<String> = _text

    private var _list = MutableLiveData<ArrayList<ReViewItem>>().apply {
        value = arrayListOf<ReViewItem>()
    }
    var clothlist: LiveData<ArrayList<ReViewItem>> = _list
}