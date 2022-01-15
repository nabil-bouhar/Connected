package com.example.connected.relations.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User
import com.example.connected.relations.RelationsRepository

class FollowingViewModel : ViewModel(), RelationsRepository.UsersListCallback {

    private val relationRepository = RelationsRepository.getInstance()

    val following: MutableLiveData<MutableList<User>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getFollowingDetails(followersIds: MutableList<String>) {
        relationRepository.getUsersDetailsListFromFireStore(followersIds, this)
    }

    override fun onReceivedUsersList(users: MutableList<User>) {
        following.value = users
        loading.value = false
    }

    override fun onErrorReceivingUsersList(error: String) {
        this.error.value = error
        loading.value = false
    }
}