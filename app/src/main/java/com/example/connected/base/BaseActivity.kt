package com.example.connected.base


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.connected.R
import com.example.connected.databinding.BaseToolbarBinding
import com.example.connected.databinding.BottomNavigationBarBinding
import com.example.connected.databinding.ToolbarBinding
import com.example.connected.home.HomeActivity
import com.example.connected.messages.MessagesActivity
import com.example.connected.profile.ProfileActivity
import com.example.connected.relations.RelationsActivity
import com.example.connected.settings.SettingsActivity

open class BaseActivity : AppCompatActivity() {

    protected fun initToolbar(toolbarBinding: ToolbarBinding, toolBarTitle: String) {
        toolbarBinding.toolbarTitle.text = toolBarTitle
        setSupportActionBar(toolbarBinding.toolbar)
    }

    protected fun initToolbar(baseToolbarBinding: BaseToolbarBinding, toolBarTitle: String) {
        baseToolbarBinding.toolbarTitle.text = toolBarTitle
        setSupportActionBar(baseToolbarBinding.toolbar)
    }

    protected fun initNavBar(bottomNavigationBarBinding: BottomNavigationBarBinding, itemId: Int) {
        bottomNavigationBarBinding.navView.menu.getItem(itemId).isChecked = true
        bottomNavigationBarBinding.navView.setOnItemSelectedListener { item ->
            val changeActivity: Intent = when (item.itemId) {
                R.id.navigation_home -> {
                    Intent(this, HomeActivity::class.java)
                }
                R.id.navigation_relations -> {
                    Intent(this, RelationsActivity::class.java)
                }
                R.id.navigation_profile -> {
                    Intent(this, ProfileActivity::class.java)
                }
                R.id.navigation_messages -> {
                    Intent(this, MessagesActivity::class.java)
                }
                else -> {
                    Intent(this, SettingsActivity::class.java)
                }
            }
            // important to avoid instantiating new activity if an instance already exists
            changeActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(changeActivity)
            false
        }
    }
}