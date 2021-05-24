package ru.spbstu.musicservice.data

import java.io.Serializable

data class User(
    val id: String,
    val firstName: String,
    val secondName: String,
    val phoneNumber: String,
    val birthday: String,
    val age: Int?,
    val email: String,
    val gender: Gender,
    val userType: UserType,
    val subscription: Subscription? = null,
    val country: Country? = null,
) : Serializable