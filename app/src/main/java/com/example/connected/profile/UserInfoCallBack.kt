package com.example.connected.profile

import com.example.connected.models.User

interface UserInfoCallBack {
    fun onReceivedCurrentUserInfo(user: User)
    fun onReceivedUserInfo(user: User)
    fun onErrorReceivingUserInfo(errorMessage: String)
    fun onUpdatedUserProfilePicture()
    fun onErrorUpdatingUserProfilePicture(errorMessage: String)
    fun onUpdatedUserStatus()
    fun onErrorUpdatingUserStatus(errorMessage: String)
    fun onUpdatedUserRelations()
    fun onErrorUpdatingUserRelations(errorMessage: String)
    fun onUpdatedUserLikes()
    fun onErrorUpdatingUserLikes(errorMessage: String)
}