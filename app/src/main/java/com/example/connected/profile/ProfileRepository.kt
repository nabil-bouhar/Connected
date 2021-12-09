package com.example.connected.profile

import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User


class ProfileRepository : BaseRepository() {

    companion object {
        private val REPO_INSTANCE: ProfileRepository = ProfileRepository()

        fun getInstance(): ProfileRepository {
            return REPO_INSTANCE
        }
    }

    private lateinit var userInfo: User

    fun getUserDataFromFS(userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(getCurrentUserUid()!!).get()
            .continueWith { it.result?.toObject(User::class.java) }.addOnCompleteListener {
                if (it.isSuccessful) {
                    userInfo = it.result!!
                    userInfoCallBack.onReceivedUserInfo(userInfo)
                } else {
                    userInfoCallBack.onErrorReceivingUserInfo(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    interface UserInfoCallBack {
        fun onReceivedUserInfo(user: User)
        fun onErrorReceivingUserInfo(errorMessage: String)
    }
}