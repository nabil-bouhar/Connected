package com.example.connected.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.utils.ConnectedUtils

class BaseViewModel : ViewModel(), BaseRepository.UserAuthenticationCallBack {

    private val baseRepository = BaseRepository.getInstance()
    private val userAuthenticationCallBack: BaseRepository.UserAuthenticationCallBack = this

    var userIsLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)
    var loginError: MutableLiveData<String> = MutableLiveData()
    var signUpError: MutableLiveData<String> = MutableLiveData()

    fun filterValidateForm(email: String, password: String) {
        if (ConnectedUtils.filterValidateEmail(email)) {
            if (ConnectedUtils.filterValidatePassword(password)) {
                baseRepository.performUserLogInAction(email, password, userAuthenticationCallBack)
            } else {
                loginError.value = ConnectedApp.appContext.getString(R.string.invalid_password)
            }
        } else {
            loginError.value = ConnectedApp.appContext.getString(R.string.invalid_email)
        }
    }

    fun signUpUser(email: String, password: String) {
        baseRepository.performUserSignUpAction(email, password, userAuthenticationCallBack)
    }

    override fun onUserLogInSuccessful() {
        userIsLoggedIn.value = true
    }

    override fun onUserLogInError(errorMessage: String) {
        loginError.value = errorMessage
    }

    override fun onUserSignUpSuccessful() {
        userIsLoggedIn.value = true
    }

    override fun onUserSignUpError(errorMessage: String) {
        signUpError.value = errorMessage
    }
}