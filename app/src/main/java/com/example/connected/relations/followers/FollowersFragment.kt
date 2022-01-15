package com.example.connected.relations.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connected.R
import com.example.connected.databinding.FollowersFragmentBinding
import com.example.connected.models.User
import com.example.connected.profile.SeeUserProfileActivity

class FollowersFragment : Fragment() {

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followersFragmentBinding: FollowersFragmentBinding
    private val followersAdapter = FollowersAdapter()
    lateinit var followersIds: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        followersFragmentBinding = FollowersFragmentBinding.inflate(layoutInflater)

        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
        followersAdapter.onItemClick = { user ->
            seeUserProfileActivity(user)
        }

        initRecyclerView()
        setObservers()

        getFollowersDetails(followersIds)
        return followersFragmentBinding.root
    }

    private fun getFollowersDetails(followersIds: MutableList<String>) {
        followersViewModel.getFollowersDetails(followersIds)
    }

    private fun seeUserProfileActivity(user: User) {
        activity?.startActivity(
            Intent(activity, SeeUserProfileActivity::class.java).putExtra(
                "user",
                user
            )
        )
    }

    private fun setObservers() {
        followersViewModel.let {
            it.followers.observe(viewLifecycleOwner, { users ->
                if (users.isNullOrEmpty()) {
                    followersFragmentBinding.tvError.text = getString(R.string.no_followers)
                } else {
                    followersAdapter.setUsers(users)
                    followersAdapter.notifyDataSetChanged()
                }
            })
            it.error.observe(viewLifecycleOwner, { error ->
                followersFragmentBinding.tvError.text = error
            })
            it.loading.observe(viewLifecycleOwner, { loading ->
                if (loading) followersFragmentBinding.progressBar.visibility = VISIBLE
                else followersFragmentBinding.progressBar.visibility = GONE
            })
        }
    }

    private fun initRecyclerView() {
        followersFragmentBinding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = followersAdapter
        }
    }
}