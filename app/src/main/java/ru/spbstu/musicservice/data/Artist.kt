package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils

data class Artist(
    val id: String,
    val name: String,
    val description: String,
    val rating: Float,
    val imageUrl: String = Utils.getRandomImage(),
    val role: Role?,
): Entity()