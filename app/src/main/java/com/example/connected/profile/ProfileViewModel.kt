package com.example.connected.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel(), UserInfoCallBack {

    private val userInfoCallBack: UserInfoCallBack = this

    var currentUserInfo: MutableLiveData<User> = MutableLiveData()
    var userInfo: MutableLiveData<User> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val dataShouldBeUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        profileRepository.getUserDataFromFireStore(null, userInfoCallBack)
    }

    fun updateProfilePicture(image: Uri) {
        profileRepository.uploadImageToFireBaseStorage(image, userInfoCallBack)
    }

    fun updateProfileStatus(status: String) {
        profileRepository.updateUserStatusInFireStore(status, userInfoCallBack)
    }

    fun updateUserRelations(
        userId: String,
        following: MutableList<String>?,
        followers: MutableList<String>?,
        friends: MutableList<String>?
    ) {
        profileRepository.updateUserRelationsInFireStore(
            userId,
            following,
            followers,
            friends,
            userInfoCallBack
        )
    }

    fun updateUserLikes(
        userId: String,
        likes: MutableList<String>?
    ) {
        profileRepository.updateUserLikesInFireStore(
            userId,
            likes,
            userInfoCallBack
        )
    }

    override fun onReceivedCurrentUserInfo(user: User) {
        currentUserInfo.value = user
        loading.value = false

    }

    override fun onReceivedUserInfo(user: User) {
        userInfo.value = user
        loading.value = false
    }

    override fun onErrorReceivingUserInfo(errorMessage: String) {
        error.value = errorMessage
        loading.value = false
    }

    override fun onUpdatedUserProfilePicture() {
        // Nothing for the moment !
    }

    override fun onErrorUpdatingUserProfilePicture(errorMessage: String) {
        // Nothing for the moment !
    }

    override fun onUpdatedUserStatus() {
        // Nothing for the moment !
    }

    override fun onErrorUpdatingUserStatus(errorMessage: String) {
        // Nothing for the moment !
    }

    override fun onUpdatedUserRelations() {
        dataShouldBeUpdated.value = true
    }

    override fun onErrorUpdatingUserRelations(errorMessage: String) {
        // Nothing for the moment !
    }

    override fun onUpdatedUserLikes() {
        dataShouldBeUpdated.value = true
    }

    override fun onErrorUpdatingUserLikes(errorMessage: String) {
        // Nothing for the moment !
    }
}