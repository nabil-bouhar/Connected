package com.example.connected.relations

import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User

class RelationsRepository : BaseRepository() {

    fun getUserInfoFromFireStore(userInfoCallBack: UserInfoCallBack) {
        getCollection(USERS_COLLECTION).document(getCurrentUserUid()!!).get()
            .continueWith { it.result?.toObject(User::class.java) }

            .addOnSuccessListener { user ->
                userInfoCallBack.onReceivedCurrentUserInfo(user!!)
            }

            .addOnFailureListener {
                userInfoCallBack.onErrorReceivingCurrentUserInfo(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    fun getUsersDetailsListFromFireStore(
        usersIds: MutableList<String>,
        usersListCallback: UsersListCallback
    ) {
        val usersList: MutableList<User> = ArrayList()
        getCollection(USERS_COLLECTION).get()

            .addOnSuccessListener { users ->
                users.forEach { queryDocumentSnapshot ->
                    val user: User = queryDocumentSnapshot.toObject(User::class.java)
                    if (usersIds.contains(user.userId)) usersList.add(user)
                }
                usersListCallback.onReceivedUsersList(usersList)
            }

            .addOnFailureListener {
                usersListCallback.onErrorReceivingUsersList(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    interface UsersListCallback {
        fun onReceivedUsersList(users: MutableList<User>)
        fun onErrorReceivingUsersList(error: String)
    }
}
