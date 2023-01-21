package com.example.guru16application.ui.shelter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guru16application.ui.food.ListViewItem

class ShelterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is shelter Fragment"
    }
    val text: LiveData<String> = _text

    private var _list = MutableLiveData<ArrayList<ListViewItem_s>>().apply {
        value = arrayListOf<ListViewItem_s>()
    }
    var list_n: LiveData<ArrayList<ListViewItem_s>> = _list
}