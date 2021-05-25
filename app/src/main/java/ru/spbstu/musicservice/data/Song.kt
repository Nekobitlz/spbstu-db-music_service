package ru.spbstu.musicservice.data

import ru.spbstu.musicservice.Utils
import java.text.SimpleDateFormat

data class Song(
    val id: String,
    val name: String,
    val length: Float,
    val releaseDate: String,
    val rating: Float,
    val albumPosition: Int,
    val playbacksCount: Int,
    val imageUrl: String = Utils.getRandomImage(),//"https://img.redbull.com/images/c_crop,x_0,y_0,h_4480,w_6720/c_fill,w_860,h_573/q_auto,f_auto/redbullcom/2018/07/17/4f3fb5c2-8f05-48a9-9720-f58d934752c2/music-collection",
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