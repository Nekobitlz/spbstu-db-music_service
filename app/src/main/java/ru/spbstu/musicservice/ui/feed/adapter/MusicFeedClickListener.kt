package ru.spbstu.musicservice.ui.feed.adapter

import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

interface MusicFeedClickListener {

    fun onChartClick(item: BaseMusicFeedRecycleItem)
    fun onCdClick(item: BaseMusicFeedRecycleItem)
    fun onPlaylistClick(item: BaseMusicFeedRecycleItem)
}