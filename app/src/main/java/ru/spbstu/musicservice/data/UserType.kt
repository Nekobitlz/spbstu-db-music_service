package ru.spbstu.musicservice.data

import java.io.Serializable

data class UserType(
    val id: String,
    val type: String,
    //val permission: UserPermission,
) : Serializable