package com.example.connected.relations

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.connected.relations.followers.FollowersFragment
import com.example.connected.relations.following.FollowingFragment
import com.example.connected.relations.friends.FriendsFragment

private const val NUM_TABS = 3

class RelationsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FollowingFragment()
            1 -> return FollowersFragment()
        }
        return FriendsFragment()
    }
}
