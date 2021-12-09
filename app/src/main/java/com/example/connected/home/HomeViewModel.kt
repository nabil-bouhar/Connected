package com.example.connected.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val homeRepository = HomeRepository.getInstance()
}