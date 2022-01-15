package com.example.connected.relations

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.connected.models.User
import com.example.connected.relations.followers.FollowersFragment
import com.example.connected.relations.following.FollowingFragment
import com.example.connected.relations.friends.FriendsFragment

private const val NUM_TABS = 3

class RelationsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var currentUserInfo: User? = null
    private var error: String? = null
    private val followingFragment = FollowingFragment()
    private val followersFragment = FollowersFragment()
    private val friendsFragment = FriendsFragment()

    fun setCurrentUserAndRelationsInfo(user: User) {
        currentUserInfo = user
        followersFragment.followersIds = currentUserInfo!!.followers!!
        friendsFragment.friendsIds = currentUserInfo!!.friends!!
    }

    fun setError(error: String) {
        this.error = error
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return followingFragment
            1 -> return followersFragment
        }
        return friendsFragment
    }

    fun onReceivedUserInfo(user: User) {
        followingFragment.getFollowingDetails(user.following!!)
    }
}
