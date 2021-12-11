package com.example.connected.profile

import com.example.connected.models.User

interface UserInfoCallBack {
    fun onReceivedUserInfo(user: User)
    fun onErrorReceivingUserInfo(errorMessage: String)
    fun onUpdatedUserProfilePicture()
    fun onErrorUpdatingUserProfilePicture(errorMessage: String)
    fun onUpdatedUserStatus()
    fun onErrorUpdatingUserStatus(errorMessage: String)
}