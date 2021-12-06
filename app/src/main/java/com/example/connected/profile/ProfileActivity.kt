package com.example.connected.profile

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, resources.getString(R.string.title_profile))
        initNavBar(binding.navBar, 3)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }
}