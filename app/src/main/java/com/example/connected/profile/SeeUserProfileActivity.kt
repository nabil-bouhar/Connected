package com.example.connected.profile

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.activity.viewModels
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivitySeeUserProfileBinding
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils
import com.squareup.picasso.Picasso

class SeeUserProfileActivity : BaseActivity() {

    private lateinit var activitySeeUserProfileBinding: ActivitySeeUserProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var user: User
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySeeUserProfileBinding = ActivitySeeUserProfileBinding.inflate(layoutInflater)
        setContentView(activitySeeUserProfileBinding.root)

        initToolbar(
            activitySeeUserProfileBinding.toolbar,
            resources.getString(R.string.title_profile)
        )
        initNavBar(activitySeeUserProfileBinding.navBar, 3)

        setListeners()
        setObservers()

        // for current user
        profileViewModel.getUserData()

        user = intent.getParcelableExtra("user")!!
        displayUserInfo(user)
    }

    private fun setObservers() {
        profileViewModel.let {
            it.currentUserInfo.observe(this, { user ->
                currentUser = user
            })
            it.dataShouldBeUpdated.observe(this, { shouldUpdate ->
                if (shouldUpdate) updateData()
            })
        }
    }

    private fun updateData() {
        startActivity(
            Intent(this, this::class.java)
                .putExtra("user", user)
                .setFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun setListeners() {
        activitySeeUserProfileBinding.apply {

            btnContact.setOnClickListener {
                // Message Activity @params(CUid, Uid)
            }

            ivFollowers.setOnClickListener {
                currentUser?.let {
                    updateUsersRelationsData()
                    handleUsersRelationsChanges()
                }
            }

            ivLikes.setOnClickListener {
                currentUser?.let {
                    updateUserLikes()
                    handleUserLikesChanges()
                }
            }
        }
    }

    private fun updateUserLikes() {
        activitySeeUserProfileBinding.apply {
            if (user.likes!!.contains(currentUser!!.userId)) {
                user.likes!!.remove(currentUser!!.userId)
                ivLikes.setImageResource(R.drawable.ic_like_icon_empty)
                tvLikes.text = user.likes!!.size.toString()
            } else {
                user.likes!!.add(currentUser!!.userId)
                ivLikes.setImageResource(R.drawable.ic_like_icon_full)
                tvLikes.text = user.likes!!.size.toString()
            }
        }
    }

    private fun handleUserLikesChanges() {
        profileViewModel.updateUserLikes(user.userId, user.likes)
    }

    private fun updateUsersRelationsData() {
        activitySeeUserProfileBinding.apply {
            if (user.followers!!.contains(currentUser!!.userId)) {

                // update user data
                user.followers!!.remove((currentUser!!.userId))
                ivFollowers.setImageResource(R.drawable.ic_user_follow)
                tvFollowers.text = user.followers!!.size.toString()
                if (user.friends!!.contains((currentUser!!.userId))) {
                    user.friends!!.remove((currentUser!!.userId))
                    tvFriends.text = user.friends!!.size.toString()
                }

                // update current user data
                currentUser!!.following!!.remove(user.userId)
                if (currentUser!!.friends!!.contains(user.userId)) {
                    currentUser!!.friends!!.remove(user.userId)
                }

            } else {

                // update user data
                user.followers!!.add((currentUser!!.userId))
                ivFollowers.setImageResource(R.drawable.ic_user_unfollow)
                tvFollowers.text = user.followers!!.size.toString()
                if (user.following!!.contains((currentUser!!.userId))) {
                    user.friends!!.add((currentUser!!.userId))
                    tvFriends.text = user.friends!!.size.toString()
                }

                // update current user data
                currentUser!!.following!!.add(user.userId)
                if (currentUser!!.followers!!.contains(user.userId)) {
                    currentUser!!.friends!!.add(user.userId)
                }
            }
        }
    }

    private fun handleUsersRelationsChanges() {
        profileViewModel.apply {
            updateUserRelations(
                user.userId,
                user.following,
                user.followers,
                user.friends
            )
            updateUserRelations(
                currentUser!!.userId,
                currentUser!!.following,
                currentUser!!.followers,
                currentUser!!.friends
            )
        }
    }

    private fun displayUserInfo(user: User) {
        activitySeeUserProfileBinding.apply {
            tvDisplayName.text = user.pseudo
            tvAge.text = ConnectedUtils.getUserAge(user.birthDate!!)
            tvLocation.text = user.city
            tvLikes.text = user.likes!!.size.toString()
            tvFollowing.text = user.following!!.size.toString()
            tvFollowers.text = user.followers!!.size.toString()
            tvFriends.text = user.friends!!.size.toString()
            if (user.about != "default") tvAbout.text = user.about
            if (user.photo != "default") {
                Picasso.get().load(user.photo).into(ivProfilePicture)
            }
            if (user.followers.contains(ConnectedApp.auth.currentUser?.uid!!)) {
                ivFollowers.setImageResource(R.drawable.ic_user_unfollow)
            }
            if (user.likes.contains(ConnectedApp.auth.currentUser?.uid!!)) {
                ivLikes.setImageResource(R.drawable.ic_like_icon_full)
            }
        }
    }
}