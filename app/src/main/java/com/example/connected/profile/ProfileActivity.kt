package com.example.connected.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityProfileBinding
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils
import com.squareup.picasso.Picasso

class ProfileActivity : BaseActivity() {

    private val activityProfileBinding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val profileViewModel: ProfileViewModel by viewModels()
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                handleUserProfilePictureChanging(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityProfileBinding.root)

        initToolbar(activityProfileBinding.toolbar, resources.getString(R.string.title_profile))
        initNavBar(activityProfileBinding.navBar, 3)

        setListeners()
        setObservers()
        profileViewModel.getUserData()
    }

    private fun setListeners() {
        with(activityProfileBinding) {
            ivEditProfilePicture.setOnClickListener {
                selectImageFromGallery()
            }
            ivEditAbout.setOnClickListener {
                showEditingView()
            }
            ivSaveAbout.setOnClickListener {
                handleProfileStatusChanging(etAbout.text.toString())
            }
            ivCancelAbout.setOnClickListener {
                hideEditingView()
            }
        }
    }

    private fun hideEditingView() {
        with(activityProfileBinding) {
            llEditAbout.visibility = GONE
            llViewAbout.visibility = VISIBLE
        }
    }

    private fun showEditingView() {
        with(activityProfileBinding) {
            etAbout.setText(tvAbout.text.toString())
            llViewAbout.visibility = GONE
            llEditAbout.visibility = VISIBLE
        }
    }

    private fun setObservers() {
        profileViewModel.let {
            it.loading.observe(this, { loading ->
                activityProfileBinding.apply {
                    if (loading) {
                        profileProgressBar.visibility = VISIBLE
                        clProfile.visibility = GONE
                    } else {
                        profileProgressBar.visibility = GONE
                        clProfile.visibility = VISIBLE
                    }
                }
            })
            it.userInfo.observe(this, { user ->
                displayUserInfo(user)
            })
            it.error.observe(this, { error ->
                activityProfileBinding.tvDisplayName.apply {
                    text = error
                    ContextCompat.getColor(applicationContext, R.color.design_default_color_error)
                }
            })
        }
    }

    private fun displayUserInfo(user: User) {
        activityProfileBinding.apply {
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
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startForResult.launch(
            Intent(
                Intent.createChooser(
                    intent,
                    getString(R.string.select_photo_intent_title)
                )
            )
        )
    }

    private fun handleUserProfilePictureChanging(image: Uri) {
        activityProfileBinding.ivProfilePicture.setImageURI(image)
        profileViewModel.updateProfilePicture(image)
    }

    private fun handleProfileStatusChanging(status: String) {
        activityProfileBinding.tvAbout.text = status
        hideEditingView()
        profileViewModel.updateProfileStatus(status)
    }
}