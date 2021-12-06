package com.example.connected.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, resources.getString(R.string.title_home))
        initNavBar(binding.navBar, 0)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }
}