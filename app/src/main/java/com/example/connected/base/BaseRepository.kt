package com.example.connected.base

import com.example.connected.R
import com.example.connected.app.ConnectedApp

class BaseRepository {

    companion object {
        private val repoInstance: BaseRepository = BaseRepository()

        fun getInstance(): BaseRepository {
            return repoInstance
        }
    }

    fun performUserLogInAction(
        email: String,
        password: String,
        userAuthenticationCallBack: UserAuthenticationCallBack
    ) {
        ConnectedApp.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userAuthenticationCallBack.onUserLogInSuccessful()
                } else {
                    userAuthenticationCallBack.onUserLogInError(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    fun performUserSignUpAction(
        email: String,
        password: String,
        userAuthenticationCallBack: UserAuthenticationCallBack
    ) {
        ConnectedApp.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userAuthenticationCallBack.onUserSignUpSuccessful()
                } else {
                    userAuthenticationCallBack.onUserSignUpError(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    interface UserAuthenticationCallBack {
        fun onUserLogInSuccessful()
        fun onUserLogInError(errorMessage: String)
        fun onUserSignUpSuccessful()
        fun onUserSignUpError(errorMessage: String)
    }
}