package com.example.connected.relations.followers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User
import com.example.connected.relations.RelationsRepository

class FollowersViewModel(private val relationRepository: RelationsRepository) : ViewModel(),
    RelationsRepository.UsersListCallback {

    val followers: MutableLiveData<MutableList<User>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getFollowersDetails(followersIds: MutableList<String>) {
        relationRepository.getUsersDetailsListFromFireStore(followersIds, this)
    }

    override fun onReceivedUsersList(users: MutableList<User>) {
        followers.value = users
        loading.value = false
    }

    override fun onErrorReceivingUsersList(error: String) {
        this.error.value = error
        loading.value = false
    }
}