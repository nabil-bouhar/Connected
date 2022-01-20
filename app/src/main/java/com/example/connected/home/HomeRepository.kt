package com.example.connected.home

import com.example.connected.R
import com.example.connected.app.ConnectedApp
import com.example.connected.base.BaseRepository
import com.example.connected.models.User

class HomeRepository : BaseRepository() {

    fun loadUsersListFromFireStore(usersCallback: UsersCallback) {
        val usersList: MutableList<User> = ArrayList()
        getCollection(USERS_COLLECTION).get()

            .addOnSuccessListener { users ->
                users.forEach { queryDocumentSnapshot ->
                    val user: User = queryDocumentSnapshot.toObject(User::class.java)
                    if (user.userId != getCurrentUserUid()) usersList.add(user)
                }
                usersCallback.onReceivedUsersList(usersList)
            }

            .addOnFailureListener {
                usersCallback.onErrorReceivingUsersList(
                    it.message
                        ?: ConnectedApp.appContext.getString(R.string.unknown_error)
                )
            }
    }

    interface UsersCallback {
        fun onReceivedUsersList(users: MutableList<User>)
        fun onErrorReceivingUsersList(error: String)
    }
}