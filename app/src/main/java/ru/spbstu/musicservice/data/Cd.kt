package ru.spbstu.musicservice.data

data class Cd(
    val id: String,
    val name: String,
    val releaseDate: String,
    val rating: Float,
    val length: Float,
) : Entity()