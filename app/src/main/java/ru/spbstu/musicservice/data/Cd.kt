package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils

data class Cd(
    val id: String,
    val name: String,
    val releaseDate: String,
    val rating: Float,
    val length: Float,
    val imageUrl: String = Utils.getRandomImage(),
) : Entity()