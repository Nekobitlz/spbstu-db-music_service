package ru.spbstu.musicservice.data

import java.io.Serializable

data class Country(
    val id: String,
    val name: String,
) : Serializable