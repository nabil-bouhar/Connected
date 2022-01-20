package com.example.connected.relations.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User
import com.example.connected.relations.RelationsRepository

class FriendsViewModel(private val relationRepository: RelationsRepository) : ViewModel(),
    RelationsRepository.UsersListCallback {

    val friends: MutableLiveData<MutableList<User>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getFriendsDetails(friendsIds: MutableList<String>) {
        relationRepository.getUsersDetailsListFromFireStore(friendsIds, this)
    }

    override fun onReceivedUsersList(users: MutableList<User>) {
        friends.value = users
        loading.value = false
    }

    override fun onErrorReceivingUsersList(error: String) {
        this.error.value = error
        loading.value = false
    }
}