package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils

data class Chart(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val version: Int,
    val imageUrl: String = Utils.getRandomImage()
) : Entity()