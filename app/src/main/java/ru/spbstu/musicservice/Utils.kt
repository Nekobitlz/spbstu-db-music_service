package ru.spbstu.musicservice

object Utils {

    fun getRandomImage(id: String, width: Int = 1000, height: Int = 1000): String {
        return "https://picsum.photos/seed/$id/$width/$height"
    }
}