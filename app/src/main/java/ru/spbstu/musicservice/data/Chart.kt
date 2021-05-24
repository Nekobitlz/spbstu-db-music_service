package ru.spbstu.musicservice.data

data class Chart(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val version: Int,
) : Entity()