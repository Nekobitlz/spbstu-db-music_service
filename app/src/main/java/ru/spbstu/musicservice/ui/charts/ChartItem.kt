package ru.spbstu.musicservice.ui.charts

import android.view.ViewGroup
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Chart
import ru.spbstu.musicservice.databinding.ItemMusicFeedChartsBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedPagerBinding
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedChartsViewHolder
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.adapter.PagerViewHolder
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

class ChartItem(
    private val item: BaseMusicFeedRecycleItem,
    private val musicFeedClickListener: MusicFeedClickListener
) : BaseAdapterItem<MusicFeedChartsViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_pager

    override fun createViewHolder(parent: ViewGroup): MusicFeedChartsViewHolder {
        return MusicFeedChartsViewHolder(
            ItemMusicFeedChartsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
    }

    override fun bind(holder: MusicFeedChartsViewHolder) {
        holder.bind(item)
    }
}