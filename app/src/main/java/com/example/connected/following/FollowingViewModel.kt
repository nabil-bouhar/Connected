package com.example.connected.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FollowingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Following fragment"
    }
    val text: LiveData<String> = _text
}