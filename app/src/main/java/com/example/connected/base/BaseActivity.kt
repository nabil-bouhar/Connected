package com.example.connected.base


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.databinding.BottomNavigationBarBinding
import com.example.connected.databinding.LoginFormBinding
import com.example.connected.databinding.RegisterFormBinding
import com.example.connected.databinding.ToolbarBinding
import com.example.connected.following.FollowingActivity
import com.example.connected.home.HomeActivity
import com.example.connected.messages.MessagesActivity
import com.example.connected.profile.ProfileActivity
import com.example.connected.settings.SettingsActivity

open class BaseActivity : AppCompatActivity() {

    private val baseViewModel: BaseViewModel by viewModels()
    private val authenticationDialog: Dialog by lazy { Dialog(this) }
    private val loginFormBinding: LoginFormBinding by lazy {
        LoginFormBinding.inflate(layoutInflater)
    }
    private val registerFormBinding: RegisterFormBinding by lazy {
        RegisterFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        ConnectedApp.auth.currentUser?.let {
            baseViewModel.userIsLoggedIn.value = true
        } ?: showLoginPopUp()
    }

    protected fun initToolbar(toolbarBinding: ToolbarBinding, toolBarTitle: String) {
        toolbarBinding.toolbarTitle.text = toolBarTitle
        setSupportActionBar(toolbarBinding.toolbar)
    }

    protected fun initNavBar(bottomNavigationBarBinding: BottomNavigationBarBinding, itemId: Int) {
        bottomNavigationBarBinding.navView.menu.getItem(itemId).isChecked = true
        bottomNavigationBarBinding.navView.setOnItemSelectedListener { item ->
            val changeActivity: Intent = when (item.itemId) {
                R.id.navigation_home -> {
                    Intent(this, HomeActivity::class.java)
                }
                R.id.navigation_following -> {
                    Intent(this, FollowingActivity::class.java)
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

    private fun showLoginPopUp() {
        loginFormBinding.apply {
            btnLogin.setOnClickListener {
                val email = this.etMail.text.toString()
                val password = this.etPassword.text.toString()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    progressbar.visibility = VISIBLE
                    baseViewModel.filterValidateForm(email, password)
                }
            }
            btnAnon.setOnClickListener {
                Toast.makeText(applicationContext, ":(", LENGTH_SHORT).show()
                // go to messages !!
            }
            btnGoRegister.setOnClickListener {
                authenticationDialog.dismiss()
                showRegisterPopUp()
            }
        }
        authenticationDialog.apply {
            setCancelable(false)
            setContentView(loginFormBinding.root)
            show()
            window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        }
    }

    private fun showRegisterPopUp() {
        registerFormBinding.apply {
            btnRegister.setOnClickListener {
                val pseudo = this.etPseudo
                val email = this.etMail.text.toString()
                val password = this.etPassword.text.toString()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    baseViewModel.signUpUser(email, password)
                }
            }
            btnGoLogin.setOnClickListener {
                authenticationDialog.dismiss()
                showLoginPopUp()
            }
        }
        authenticationDialog.apply {
            setCancelable(false)
            setContentView(registerFormBinding.root)
            show()
            window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        }
    }

    private fun setObservers() {
        baseViewModel.let { it ->
            it.userIsLoggedIn.observe(this, {
                if (it) authenticationDialog.dismiss()
            })
            it.loginError.observe(this, { error ->
                loginFormBinding.tvError.text = error
                loginFormBinding.progressbar.visibility = GONE
            })
            it.signUpError.observe(this, { error ->
                registerFormBinding.tvError.text = error
                registerFormBinding.progressbar.visibility = GONE
            })
        }
    }
}