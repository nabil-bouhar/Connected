package com.example.connected.profile

import android.net.Uri
import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User
import com.google.firebase.storage.FirebaseStorage

class ProfileRepository : BaseRepository() {

    fun getUserDataFromFireStore(userId: String?, userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(userId ?: getCurrentUserUid()!!).get()
            .continueWith { it.result?.toObject(User::class.java) }

            .addOnSuccessListener { user ->
                userId?.let {
                    userInfoCallBack.onReceivedUserInfo(user!!)
                } ?: userInfoCallBack.onReceivedCurrentUserInfo(user!!)
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorReceivingUserInfo(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    fun updateUserStatusInFireStore(status: String, userInfoCallBack: UserInfoCallBack) {
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

    fun updateUserRelationsInFireStore(
        userId: String,
        following: MutableList<String>?,
        followers: MutableList<String>?,
        friends: MutableList<String>?,
        userInfoCallBack: UserInfoCallBack
    ) {
        getCollection(USERS_COLLECTION).document(userId).update(
            "following", following,
            "followers", followers,
            "friends", friends
        )

            .addOnSuccessListener {
                if (userId != ConnectedApp.auth.currentUser?.uid!!) userInfoCallBack.onUpdatedUserRelations()
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorUpdatingUserRelations(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    fun updateUserLikesInFireStore(
        userId: String,
        likes: MutableList<String>?,
        userInfoCallBack: UserInfoCallBack
    ) {
        getCollection(USERS_COLLECTION).document(userId).update("likes", likes)

            .addOnSuccessListener {
                userInfoCallBack.onUpdatedUserLikes()
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorUpdatingUserLikes(
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