package ru.spbstu.musicservice.data

data class Payment(
    val id: String,
    val date: String,
    val amount: Int,
) : Entity()