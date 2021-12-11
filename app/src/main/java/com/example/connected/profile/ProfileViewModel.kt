package com.example.connected.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.models.User
import com.example.connected.utils.ConnectedUtils

class ProfileViewModel : ViewModel(), UserInfoCallBack {

    private val profileRepository = ProfileRepository.getInstance()
    private val userInfoCallBack: UserInfoCallBack = this

    var userInfo: MutableLiveData<User> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val error: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        profileRepository.getUserDataFromFireStore(userInfoCallBack)
    }

    fun getUserAge(dob: Long): String {
        return ConnectedUtils.getNumberOfYearsBetweenTwoDates(System.currentTimeMillis(), dob)
            .toString() + " " + ConnectedApp.appContext.getString(R.string.years)
    }

    fun updateProfilePicture(image: Uri) {
        profileRepository.uploadImageToFireBaseStorage(image, userInfoCallBack)
    }

    fun updateProfileStatus(status: String) {
        profileRepository.updateProfileStatusInFireStore(status, userInfoCallBack)
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
}