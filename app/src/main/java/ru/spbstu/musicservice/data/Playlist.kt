package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils

data class Playlist(
    val id: String,
    val name: String,
    val updateDate: String,
    val playbacksCount: Int,
    val imageUrl: String = Utils.getRandomImage(),
) : Entity()