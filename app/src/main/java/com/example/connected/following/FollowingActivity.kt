package com.example.connected.following

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityFollowingBinding

class FollowingActivity : BaseActivity() {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var binding: ActivityFollowingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, resources.getString(R.string.title_following))
        initNavBar(binding.navBar, 2)

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
    }
}