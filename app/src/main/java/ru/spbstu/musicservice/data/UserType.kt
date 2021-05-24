package ru.spbstu.musicservice.data

data class UserType(
    val id: String,
    val type: String,
    //val permission: UserPermission,
) : Entity()