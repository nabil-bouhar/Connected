package com.example.connected.relations

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityRelationsBinding
import com.google.android.material.tabs.TabLayoutMediator

val titles = arrayOf(
    ConnectedApp.appContext.getString(R.string.following),
    ConnectedApp.appContext.getString(R.string.followers),
    ConnectedApp.appContext.getString(R.string.friends)
)
val icons = arrayOf(
    R.drawable.ic_following_24,
    R.drawable.ic_followers_24,
    R.drawable.ic_friends_24
)

class RelationsActivity : BaseActivity() {

    private lateinit var relationsViewModel: RelationsViewModel
    private lateinit var binding: ActivityRelationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityRelationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar(binding.toolbar, resources.getString(R.string.title_relations))
        initNavBar(binding.navBar, 2)
        initRelationsTabLayout()

        relationsViewModel = ViewModelProvider(this)[RelationsViewModel::class.java]
    }

    private fun initRelationsTabLayout() {

        val viewPager = binding.vpRelations
        val tabLayout = binding.tlRelations
        val adapter = RelationsViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
            tab.icon = ContextCompat.getDrawable(this, icons[position])
        }.attach()
    }
}