package ru.spbstu.musicservice.ui.feed.item

import androidx.annotation.IdRes
import ru.spbstu.musicservice.Utils
import ru.spbstu.musicservice.data.Entity

data class BaseMusicFeedRecycleItem(
    @IdRes val viewType: Int,
    val id: String,
    val title: String,
    val imageUrl: String = Utils.getRandomImage(id),
    val rating: Float = 0.0f,
    val entity: Entity? = null,
)