package com.example.connected.authentication

import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthenticationRepository {

    companion object {
        private val REPO_INSTANCE: UserAuthenticationRepository = UserAuthenticationRepository()
        private const val COLLECTION_NAME = "users"

        fun getInstance(): UserAuthenticationRepository {
            return REPO_INSTANCE
        }
    }

    private fun getCurrentUserUid(): String? {
        return ConnectedApp.auth.currentUser?.uid
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    private fun createUserInFireStore(
        user: User,
        userAuthenticationCallBack: UserAuthenticationCallBack
    ) {
        getCurrentUserUid()?.let {
            getUsersCollection().document(it).set(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userAuthenticationCallBack.onUserLogInSuccessful(it)
                } else {
                    userAuthenticationCallBack.onUserLogInError(
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
                        pseudo,
                        contactNo,
                        email,
                        password,
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