package ru.spbstu.musicservice.data

data class Playlist(
    val id: String,
    val name: String,
    val updateDate: String,
    val playbacksCount: Int,
) : Entity()