package com.example.connected.relations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User

class RelationsViewModel : ViewModel(), UserInfoCallBack {
    private val relationsRepository = RelationsRepository.getInstance()
    private val userInfoCallBack: UserInfoCallBack = this

    val currentUserInfo: MutableLiveData<User> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun getUserInfo() {
        relationsRepository.getUserInfoFromFireStore(userInfoCallBack)
    }

    override fun onReceivedCurrentUserInfo(user: User) {
        currentUserInfo.value = user
    }

    override fun onErrorReceivingCurrentUserInfo(errorMessage: String) {
        error.value = errorMessage
    }
}