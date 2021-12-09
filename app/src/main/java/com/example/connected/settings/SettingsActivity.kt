package com.example.connected.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivitySettingsBinding
import com.example.connected.home.HomeActivity
import com.example.connected.utils.ConnectedUtils

class SettingsActivity : BaseActivity() {

    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initToolbar(binding.toolbar, resources.getString(R.string.title_settings))
        initNavBar(binding.navBar, 4)

        setListener()
    }

    private fun setListener() {
        binding.clLogOut.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                .setMessage(getString(R.string.log_out_conf_mess))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    logOut()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun logOut() {
        ConnectedApp.auth.signOut()
        ConnectedUtils.changeActivityAndClearStack(
            this@SettingsActivity,
            HomeActivity::class.java,
            null
        )
    }
}