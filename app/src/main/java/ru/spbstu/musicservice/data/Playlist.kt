package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils

data class Playlist(
    val id: String,
    val name: String,
    val updateDate: String,
    val playbacksCount: Int,
    val imageUrl: String = Utils.getRandomImage(),
) : Entity() {
    override fun toString(): String {
        return "Плейлист\nНазвание: '$name'\nДата обновления: '$updateDate'\nКоличество воспроизведений: $playbacksCount\nОбложка: $imageUrl"
    }
}