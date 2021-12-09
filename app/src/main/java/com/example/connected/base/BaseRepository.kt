package com.example.connected.base

import com.example.connected.app.ConnectedApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


open class BaseRepository {

    companion object {
        const val USERS_COLLECTION = "users"
        const val MESSAGES_COLLECTION = "messages"
    }

    protected fun getCurrentUserUid(): String? {
        return ConnectedApp.auth.currentUser?.uid
    }

    protected fun getCollection(collectionName: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(collectionName)
    }
}