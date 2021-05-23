package ru.spbstu.musicservice.data

import java.io.Serializable

data class Gender(
    val id: String,
    val gender: String,
) : Serializable