package ru.spbstu.musicservice.data

import java.io.Serializable

data class Subscription(
    val id: String,
    val price: Int,
    val startDate: String,
    val endDate: String,
) : Serializable