package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils
import java.text.SimpleDateFormat

data class Song(
    val id: String,
    val name: String,
    val length: Float,
    val releaseDate: String,
    val rating: Float,
    var albumPosition: Int,
    val playbacksCount: Int,
    val imageUrl: String = Utils.getRandomImage(id),
    val artist: Artist?,
    /*val album: Album?,*/
    val genre: Genre?,
) : Entity() {

    val stringLength: String
        get() = SimpleDateFormat("mm:ss").format(length); //String.format("%f:%02f:%02f", length / 3600, (length % 3600) / 60, (length % 60))

    override fun toString(): String {
        return "\nПесня\nНазвание='$name'\nДлительность=$stringLength\nДата публикации='$releaseDate'\nРейтинг=$rating\nПозиция в альбоме=$albumPosition\nКоличество прослушиваний=$playbacksCount\nОбложка=$imageUrl\nИсполнитель=${artist?.name}\nЖанр=${genre?.name}\n"
    }


}