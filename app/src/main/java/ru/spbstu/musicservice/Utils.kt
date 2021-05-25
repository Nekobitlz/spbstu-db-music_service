package ru.spbstu.musicservice

import kotlin.random.Random

object Utils {

    fun getRandomImage(width: Int = 1000, height: Int = 1000): String {
        return "https://picsum.photos/seed/${Random.nextInt()}/$width/$height"
    }
}