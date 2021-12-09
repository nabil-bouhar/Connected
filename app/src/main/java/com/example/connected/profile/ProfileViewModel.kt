package com.example.connected.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User

class ProfileViewModel : ViewModel(), ProfileRepository.UserInfoCallBack {

    private val profileRepository = ProfileRepository.getInstance()
    private val userInfoCallBack: ProfileRepository.UserInfoCallBack = this

    var userInfo: MutableLiveData<User> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val error: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        profileRepository.getUserDataFromFS(userInfoCallBack)
    }

    override fun onReceivedUserInfo(user: User) {
        userInfo.value = user
        loading.value = false

    }

    override fun onErrorReceivingUserInfo(errorMessage: String) {
        error.value = errorMessage
        loading.value = false
    }

}