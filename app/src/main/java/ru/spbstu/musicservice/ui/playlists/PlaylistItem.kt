package ru.spbstu.musicservice.ui.playlists

import android.view.ViewGroup
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedPlaylistsBinding
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedPlaylistsViewHolder
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

class PlaylistItem(
    private val item: BaseMusicFeedRecycleItem,
    private val musicFeedClickListener: MusicFeedClickListener,
) : BaseAdapterItem<MusicFeedPlaylistsViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_pager

    override fun createViewHolder(parent: ViewGroup): MusicFeedPlaylistsViewHolder {
        return MusicFeedPlaylistsViewHolder(
            ItemMusicFeedPlaylistsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
    }

    override fun bind(holder: MusicFeedPlaylistsViewHolder) {
        holder.bind(item)
    }
}