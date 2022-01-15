package com.example.connected.relations.friends

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connected.R
import com.example.connected.databinding.FriendsFragmentBinding
import com.example.connected.models.User
import com.example.connected.profile.SeeUserProfileActivity

class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var friendsFragmentBinding: FriendsFragmentBinding
    private val friendsAdapter = FriendsAdapter()
    lateinit var friendsIds: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        friendsFragmentBinding = FriendsFragmentBinding.inflate(layoutInflater)

        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        friendsAdapter.onItemClick = { user ->
            seeUserProfileActivity(user)
        }

        initRecyclerView()
        setObservers()

        getFriendsDetails(friendsIds)
        return friendsFragmentBinding.root
    }

    private fun getFriendsDetails(friendsIds: MutableList<String>) {
        friendsViewModel.getFriendsDetails(friendsIds)
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
        friendsViewModel.let {
            it.friends.observe(viewLifecycleOwner, { users ->
                if (users.isNullOrEmpty()) {
                    friendsFragmentBinding.tvError.text = getString(R.string.no_friends)
                } else {
                    friendsAdapter.setUsers(users)
                    friendsAdapter.notifyDataSetChanged()
                }
            })
            it.error.observe(viewLifecycleOwner, { error ->
                friendsFragmentBinding.tvError.text = error
            })
            it.loading.observe(viewLifecycleOwner, { loading ->
                if (loading) friendsFragmentBinding.progressBar.visibility = View.VISIBLE
                else friendsFragmentBinding.progressBar.visibility = View.GONE
            })
        }
    }

    private fun initRecyclerView() {
        friendsFragmentBinding.rvFriends.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = friendsAdapter
        }
    }
}