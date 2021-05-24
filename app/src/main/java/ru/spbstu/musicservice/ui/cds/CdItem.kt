package ru.spbstu.musicservice.ui.cds

import android.view.ViewGroup
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedCdsBinding
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedCdsViewHolder
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

class CdItem(
    private val item: BaseMusicFeedRecycleItem,
    private val musicFeedClickListener: MusicFeedClickListener,
) : BaseAdapterItem<MusicFeedCdsViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_pager

    override fun createViewHolder(parent: ViewGroup): MusicFeedCdsViewHolder {
        return MusicFeedCdsViewHolder(
            ItemMusicFeedCdsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
    }

    override fun bind(holder: MusicFeedCdsViewHolder) {
        holder.bind(item)
    }
}