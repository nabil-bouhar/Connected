package com.example.connected.home

import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.authentication.UserAuthenticationViewModel
import com.example.connected.base.BaseActivity
import com.example.connected.databinding.ActivityHomeBinding
import com.example.connected.databinding.LoginFormBinding
import com.example.connected.databinding.RegisterFormBinding
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

class HomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val activityHomeBinding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val userAuthenticationViewModel: UserAuthenticationViewModel by viewModels()
    private val authenticationDialog: Dialog by lazy { Dialog(this) }
    private val loginFormBinding: LoginFormBinding by lazy {
        LoginFormBinding.inflate(layoutInflater)
    }
    private val registerFormBinding: RegisterFormBinding by lazy {
        RegisterFormBinding.inflate(layoutInflater)
    }
    private val homeAdapter = HomeAdapter()

    private var users: MutableList<User> = ArrayList()
    private var currentSelectedDate: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setContentView(activityHomeBinding.root)
            initToolbar(activityHomeBinding.toolbar, resources.getString(R.string.title_home))
            initNavBar(activityHomeBinding.navBar, 0)
        }
        initRecyclerView()
        setObservers()
        homeViewModel.getUsers()
    }

    private fun initRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        activityHomeBinding.rvUsers.apply {
            this.layoutManager = layoutManager
            addItemDecoration(HomePageItemDecoration(5, 2))
            itemAnimator = DefaultItemAnimator()
            adapter = homeAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        ConnectedApp.auth.currentUser?.let {
            userAuthenticationViewModel.userIsLoggedIn.value = true
        } ?: showLoginPopUp()
    }

    override fun onPause() {
        super.onPause()
        authenticationDialog.dismiss()
    }

    private fun showLoginPopUp() {
        loginFormBinding.apply {
            btnLogin.setOnClickListener {
                val email = this.etMail.text.toString()
                val password = this.etPassword.text.toString()
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    progressbar.visibility = View.VISIBLE
                    userAuthenticationViewModel.filterValidateForm(email, password)
                }
            }
            btnAnon.setOnClickListener {
                Toast.makeText(applicationContext, ":(", Toast.LENGTH_SHORT).show()
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
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun showRegisterPopUp() {
        registerFormBinding.apply {
            btnRegister.setOnClickListener {
                if (!emptyRegisterForm()) {
                    userAuthenticationViewModel.filterValidateForm(
                        rbFemale.isChecked,
                        etName.text.toString(),
                        etBirthDate.text.toString(),
                        etLocation.text.toString(),
                        etPseudo.text.toString(),
                        etContactNo.text.toString(),
                        etMail.text.toString(),
                        etPassword.text.toString(),
                        etRePassword.text.toString()
                    )
                } else {
                    tvError.text = getString(R.string.empty_field_in_form_message)
                }
            }
            etBirthDate.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePicker()
                    etBirthDate.inputType = InputType.TYPE_NULL
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
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun emptyRegisterForm(): Boolean {
        return with(registerFormBinding) {
            TextUtils.isEmpty(etName.text)
                    || TextUtils.isEmpty(etBirthDate.text.toString())
                    || TextUtils.isEmpty(etLocation.text.toString())
                    || TextUtils.isEmpty(etPseudo.text.toString())
                    || TextUtils.isEmpty(etContactNo.text.toString())
                    || TextUtils.isEmpty(etMail.text.toString())
                    || TextUtils.isEmpty(etPassword.text.toString())
                    || TextUtils.isEmpty(etRePassword.text.toString())
        }
    }

    private fun showDatePicker() {
        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()

        MaterialDatePicker.Builder.datePicker().setSelection(selectedDateInMillis)
            //.setTheme(R.style.datePickerTheme)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(object : CalendarConstraints.DateValidator {
                        override fun isValid(date: Long): Boolean {
                            return date >= ConnectedUtils.getBirthMinDate() && date <= ConnectedUtils.getBirthMaxDate()
                        }

                        override fun writeToParcel(p0: Parcel?, p1: Int) {
                            // Not Needed !
                        }

                        override fun describeContents(): Int {
                            return 0
                        }
                    }).build()
            )
            .build().apply {
                addOnPositiveButtonClickListener { dateInMillis -> onDateSelected(dateInMillis) }
            }.show(supportFragmentManager, MaterialDatePicker::class.java.canonicalName)
    }

    private fun onDateSelected(dateTimeStampInMillis: Long) {
        registerFormBinding.etBirthDate.setText(
            ConnectedUtils.convertLongToTime(
                dateTimeStampInMillis
            )
        )
    }

    private fun setObservers() {
        userAuthenticationViewModel.let { it ->
            it.userIsLoggedIn.observe(this, {
                if (it) authenticationDialog.dismiss()
            })
            it.loginError.observe(this, { error ->
                loginFormBinding.tvError.text = error
                loginFormBinding.progressbar.visibility = View.GONE
            })
            it.signUpError.observe(this, { error ->
                registerFormBinding.tvError.text = error
                registerFormBinding.progressbar.visibility = View.GONE
            })
        }
        homeViewModel.let {
            it.users.observe(this, { users ->
                this.users = users
                homeAdapter.setUsers(users)
                homeAdapter.notifyDataSetChanged()
            })
            it.error.observe(this, {
                // handle error
            })
            it.loading.observe(this, { loading ->
                if (loading) {
                    activityHomeBinding.homeProgressBar.visibility = View.VISIBLE
                } else {
                    activityHomeBinding.homeProgressBar.visibility = View.GONE
                    activityHomeBinding.tvYmk.visibility = View.VISIBLE
                }
            })
        }
    }
}
