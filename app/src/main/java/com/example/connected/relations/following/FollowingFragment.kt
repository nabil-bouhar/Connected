package com.example.connected.relations.following

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connected.R
import com.example.connected.databinding.FollowingFragmentBinding
import com.example.connected.models.User
import com.example.connected.profile.SeeUserProfileActivity
import org.koin.android.ext.android.inject

class FollowingFragment : Fragment() {

    private val followingViewModel: FollowingViewModel by inject()
    private lateinit var followingFragmentBinding: FollowingFragmentBinding
    private val followingAdapter = FollowingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        followingFragmentBinding = FollowingFragmentBinding.inflate(layoutInflater)

        followingAdapter.onItemClick = { user ->
            seeUserProfileActivity(user)
        }

        initRecyclerView()
        setObservers()

        return followingFragmentBinding.root
    }

    fun getFollowingDetails(followingIds: MutableList<String>) {
        followingViewModel.getFollowingDetails(followingIds)
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
        followingViewModel.let {
            it.following.observe(viewLifecycleOwner, { users ->
                if (users.isNullOrEmpty()) {
                    followingFragmentBinding.tvError.text = getString(R.string.no_following)
                } else {
                    followingAdapter.setUsers(users)
                    followingAdapter.notifyDataSetChanged()
                }
            })
            it.error.observe(viewLifecycleOwner, { error ->
                followingFragmentBinding.tvError.text = error
            })
            it.loading.observe(viewLifecycleOwner, { loading ->
                if (loading) followingFragmentBinding.progressBar.visibility = View.VISIBLE
                else followingFragmentBinding.progressBar.visibility = View.GONE
            })
        }
    }

    private fun initRecyclerView() {
        followingFragmentBinding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = followingAdapter
        }
    }
}