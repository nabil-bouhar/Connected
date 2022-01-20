package com.example.connected.authentication

import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User

class UserAuthenticationRepository : BaseRepository() {

    private fun createUserInFireStore(
        user: User,
        userAuthenticationCallBack: UserAuthenticationCallBack
    ) {
        getCurrentUserUid()?.let {
            getCollection(USERS_COLLECTION).document(it).set(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userAuthenticationCallBack.onUserSignUpSuccessful(it)
                } else {
                    userAuthenticationCallBack.onUserSignUpError(
                        task.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
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
                    val userId = ConnectedApp.auth.currentUser!!.uid
                    userAuthenticationCallBack.onUserLogInSuccessful(userId)
                } else {
                    userAuthenticationCallBack.onUserLogInError(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    fun performUserSignUpAction(
        gender: Int,
        name: String,
        birthDate: Long,
        location: String,
        pseudo: String,
        contactNo: String,
        email: String,
        password: String,
        userAuthenticationCallBack: UserAuthenticationCallBack
    ) {
        ConnectedApp.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = User(
                        getCurrentUserUid()!!,
                        gender,
                        name,
                        birthDate,
                        location,
                        pseudo,
                        contactNo,
                        email,
                        "default",
                        "default",
                        "default",
                        ArrayList(),
                        ArrayList(),
                        ArrayList(),
                        ArrayList()
                    )
                    createUserInFireStore(user, userAuthenticationCallBack)
                } else {
                    userAuthenticationCallBack.onUserSignUpError(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    interface UserAuthenticationCallBack {
        fun onUserLogInSuccessful(userId: String)
        fun onUserLogInError(errorMessage: String)
        fun onUserSignUpSuccessful(userId: String)
        fun onUserSignUpError(errorMessage: String)
    }
}