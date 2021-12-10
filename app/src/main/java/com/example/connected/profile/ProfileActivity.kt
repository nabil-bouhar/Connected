package com.example.connected.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.connected.R
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityProfileBinding
import com.example.connected.models.User

class ProfileActivity : BaseActivity() {

    private val activityProfileBinding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityProfileBinding.root)

        initToolbar(activityProfileBinding.toolbar, resources.getString(R.string.title_profile))
        initNavBar(activityProfileBinding.navBar, 3)

        setObservers()
        profileViewModel.getUserData()
    }

    private fun setObservers() {
        profileViewModel.let {
            it.loading.observe(this, { loading ->
                activityProfileBinding.apply {
                    if (loading) {
                        profileProgressBar.visibility = View.VISIBLE
                        clProfile.visibility = View.GONE
                    } else {
                        profileProgressBar.visibility = View.GONE
                        clProfile.visibility = View.VISIBLE
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
            tvLikes.text = user.likes!!.size.toString()
            tvFollowing.text = user.following!!.size.toString()
            tvFollowers.text = user.followers!!.size.toString()
            tvFriends.text = user.friends!!.size.toString()
            tvAbout.text =
                if (user.about == "default") getText(R.string.about_default_mesage) else user.about
        }
    }
}