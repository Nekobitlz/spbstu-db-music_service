package ru.spbstu.musicservice.data

data class Subscription(
    val id: String,
    val price: Int,
    val startDate: String,
    val endDate: String,
) : Entity()