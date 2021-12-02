package com.example.connected.base

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.connected.R
import com.example.connected.databinding.ActivityBaseBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var customizedToolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        initToolbar()
        initNavBar()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        customizedToolbarTitle = findViewById(R.id.toolbar_title)
        customizedToolbarTitle.text = resources.getString(R.string.title_home)
        setSupportActionBar(toolbar)
    }

    private fun initNavBar() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_messages,
                R.id.navigation_following,
                R.id.navigation_profile,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            customizedToolbarTitle.text = item.title
            navController.navigate(item.itemId)
            true
        }
    }
}