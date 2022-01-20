package com.example.connected.relations

import android.os.Bundle
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityRelationsBinding
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class RelationsActivity : BaseActivity() {

    private lateinit var activityRelationsBinding: ActivityRelationsBinding
    private lateinit var relationsViewPagerAdapter: RelationsViewPagerAdapter
    private val relationsViewModel: RelationsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityRelationsBinding = ActivityRelationsBinding.inflate(layoutInflater)
        setContentView(activityRelationsBinding.root)

        initToolbar(activityRelationsBinding.toolbar, resources.getString(R.string.title_relations))
        initNavBar(activityRelationsBinding.navBar, 2)
        initRelationsTabLayout()
        relationsViewModel.getUserInfo()
        setObservers()
    }

    private fun setObservers() {
        relationsViewModel.let {
            it.currentUserInfo.observe(this, { user ->
                user?.let {
                    relationsViewPagerAdapter.setCurrentUserAndRelationsInfo(user)
                    relationsViewPagerAdapter.onReceivedUserInfo(user)
                }
            })
            it.error.observe(this, { error ->
                relationsViewPagerAdapter.setError(error)
            })
        }
    }

    private fun initRelationsTabLayout() {

        val viewPager = activityRelationsBinding.vpRelations
        val tabLayout = activityRelationsBinding.tlRelations

        relationsViewPagerAdapter = RelationsViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = relationsViewPagerAdapter

        tabLayout.apply {
            addTab(
                tabLayout.newTab().setText(ConnectedApp.appContext.getString(R.string.following))
            )
            addTab(
                tabLayout.newTab().setText(ConnectedApp.appContext.getString(R.string.followers))
            )
            addTab(tabLayout.newTab().setText(ConnectedApp.appContext.getString(R.string.friends)))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // Nothing for the moment
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // Nothing for the moment
                }
            })
        }
    }
}