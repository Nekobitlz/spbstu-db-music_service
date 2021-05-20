package ru.spbstu.musicservice.ui.feed.item

import androidx.annotation.IdRes

data class BaseMusicFeedRecycleItem(
    @IdRes val viewType: Int,
    val id: String,
    val title: String,
    val imageUrl: String? = "https://pbs.twimg.com/profile_banners/1231599602207469570/1595272939/1500x500",
    val rating: Float = 0.0f
)