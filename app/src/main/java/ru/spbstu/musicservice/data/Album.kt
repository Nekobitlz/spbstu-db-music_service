package ru.spbstu.musicservice.data

data class Album(
    val id: String,
    val name: String,
    val length: Float,
    val releaseDate: String,
    val rating: Float,
    val albumPosition: Int,
    val playbacksCount: Int,
    val imageUrl: String,
    val genre: Genre?,
) : Entity()