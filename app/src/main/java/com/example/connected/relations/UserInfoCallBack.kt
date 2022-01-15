package com.example.connected.relations

import com.example.connected.models.User

interface UserInfoCallBack {

    fun onReceivedCurrentUserInfo(user: User)
    fun onErrorReceivingCurrentUserInfo(errorMessage: String)
}