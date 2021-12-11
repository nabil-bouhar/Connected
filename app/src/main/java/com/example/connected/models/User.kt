package com.example.connected.models

data class User(
    val userId: String,
    val gender: Int?,
    val name: String?,
    val birthDate: Long?,
    val city: String?,
    val pseudo: String?,
    val contactNo: String?,
    val email: String?,
    val about: String?,
    val photo: String?,
    val thumbPhoto: String?,
    val likes: MutableList<String>?,
    val following: MutableList<String>?,
    val followers: MutableList<String>?,
    val friends: MutableList<String>?
) {
    // no args constructor is needed to deserialize Firestore DocumentSnapshot to local User object
    constructor() : this(
        "",
        -1,
        "",
        -1,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ArrayList(),
        ArrayList(),
        ArrayList(),
        ArrayList()
    )
}