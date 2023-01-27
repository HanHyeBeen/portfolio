package com.example.guru16application.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guru16application.ui.clothing.ReViewItem

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _list = MutableLiveData<ArrayList<TodoViewItem>>().apply {
        value = arrayListOf<TodoViewItem>()
    }
    var todolist : LiveData<ArrayList<TodoViewItem>> = _list
}