package com.example.connected.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.utils.ConnectedUtils

class UserAuthenticationViewModel : ViewModel(),
    UserAuthenticationRepository.UserAuthenticationCallBack {

    private val userAuthenticationRepository = UserAuthenticationRepository.getInstance()
    private val userAuthenticationCallBack: UserAuthenticationRepository.UserAuthenticationCallBack =
        this

    var userIsLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)
    var loginError: MutableLiveData<String> = MutableLiveData()
    var signUpError: MutableLiveData<String> = MutableLiveData()
    val loading = MutableLiveData(true)

    fun filterValidateForm(email: String, password: String) {
        if (ConnectedUtils.filterValidateEmail(email)) {
            if (ConnectedUtils.filterValidatePassword(password)) {
                userAuthenticationRepository.performUserLogInAction(
                    email,
                    password,
                    userAuthenticationCallBack
                )
            } else {
                loginError.value = ConnectedApp.appContext.getString(R.string.invalid_password)
            }
        } else {
            loginError.value = ConnectedApp.appContext.getString(R.string.invalid_email)
        }
    }

    fun filterValidateForm(
        gender: Boolean,
        name: String,
        birthDate: String,
        location: String,
        pseudo: String,
        contactNo: String,
        email: String,
        password1: String,
        password2: String
    ) {
        if (ConnectedUtils.filterValidateName(name)) {
            if (ConnectedUtils.filterValidateCity(location)) {
                if (ConnectedUtils.filterValidatePseudonym(pseudo)) {
                    if (ConnectedUtils.filterValidatePhoneNumber(contactNo)) {
                        if (ConnectedUtils.filterValidateEmail(email)) {
                            if (ConnectedUtils.filterValidatePassword(password1)) {
                                if (password1 == password2) {
                                    userAuthenticationRepository.performUserSignUpAction(
                                        ConnectedUtils.booleanToInt(gender),
                                        name,
                                        ConnectedUtils.convertDateToLong(birthDate),
                                        location,
                                        pseudo,
                                        contactNo,
                                        email,
                                        password1,
                                        userAuthenticationCallBack
                                    )
                                } else {
                                    signUpError.value =
                                        ConnectedApp.appContext.getString(R.string.passwords_do_not_match)
                                }
                            } else {
                                signUpError.value =
                                    ConnectedApp.appContext.getString(R.string.invalid_password)
                            }
                        } else {
                            signUpError.value =
                                ConnectedApp.appContext.getString(R.string.invalid_email)
                        }
                    } else {
                        signUpError.value =
                            ConnectedApp.appContext.getString(R.string.invalid_contact_no)
                    }
                } else {
                    signUpError.value = ConnectedApp.appContext.getString(R.string.invalid_pseudo)
                }
            } else {
                signUpError.value = ConnectedApp.appContext.getString(R.string.invalid_location)
            }
        } else {
            signUpError.value = ConnectedApp.appContext.getString(R.string.invalid_name)
        }
    }

    override fun onUserLogInSuccessful(userId: String) {
        userIsLoggedIn.value = true
        loading.value = false
    }

    override fun onUserLogInError(errorMessage: String) {
        loginError.value = errorMessage
        loading.value = false
    }

    override fun onUserSignUpSuccessful(userId: String) {
        userIsLoggedIn.value = true
        loading.value = false
    }

    override fun onUserSignUpError(errorMessage: String) {
        signUpError.value = errorMessage
        loading.value = false
    }
}