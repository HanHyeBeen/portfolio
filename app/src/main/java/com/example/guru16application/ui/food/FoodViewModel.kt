package com.example.guru16application.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodViewModel : ViewModel() {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is food Fragment"
    }
    val text: LiveData<String> = _text*/

    private var _list = MutableLiveData<ArrayList<ListViewItem>>().apply {
        value = arrayListOf<ListViewItem>()
    }
    var list_n: LiveData<ArrayList<ListViewItem>> = _list
}