package com.example.connected.profile

import android.net.Uri
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User
import com.google.firebase.storage.FirebaseStorage


class ProfileRepository : BaseRepository() {

    companion object {
        private val REPO_INSTANCE: ProfileRepository = ProfileRepository()

        fun getInstance(): ProfileRepository {
            return REPO_INSTANCE
        }
    }

    fun getUserDataFromFireStore(userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(getCurrentUserUid()!!).get()
            .continueWith { it.result?.toObject(User::class.java) }.addOnCompleteListener {
                if (it.isSuccessful) {
                    userInfoCallBack.onReceivedUserInfo(it.result!!)
                } else {
                    userInfoCallBack.onErrorReceivingUserInfo(
                        it.exception?.message
                            ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                    )
                }
            }
    }

    fun updateProfileStatusInFireStore(status: String, userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(getCurrentUserUid()!!).update("about", status)
            .addOnSuccessListener {
                userInfoCallBack.onUpdatedUserStatus()
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorUpdatingUserStatus(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    private fun updateImageUrlInFireStore(imageUrl: String, userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(getCurrentUserUid()!!).update("photo", imageUrl)
            .addOnSuccessListener {
                userInfoCallBack.onUpdatedUserProfilePicture()
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorUpdatingUserProfilePicture(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    fun uploadImageToFireBaseStorage(image: Uri, userInfoCallBack: UserInfoCallBack) {
        val fileName = getCurrentUserUid() + ".jpg"
        FirebaseStorage.getInstance().reference.child("images/$fileName")
            .putFile(image)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    updateImageUrlInFireStore(it.toString(), userInfoCallBack)
                }
            }

            .addOnFailureListener { exception ->
                userInfoCallBack.onErrorUpdatingUserProfilePicture(
                    exception.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }
}