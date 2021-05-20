package ru.spbstu.musicservice.ui.feed.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.DummyViewHolder
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedCdsBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedChartsBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedPlaylistsBinding
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

class PagerAdapter(
    private val musicFeedClickListener: MusicFeedClickListener
) : ListAdapter<BaseMusicFeedRecycleItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.id.view_type_music_feed_playlists -> MusicFeedPlaylistsViewHolder(
            ItemMusicFeedPlaylistsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
        R.id.view_type_music_feed_charts -> MusicFeedChartsViewHolder(
            ItemMusicFeedChartsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
        R.id.view_type_music_feed_cds -> MusicFeedCdsViewHolder(
            ItemMusicFeedCdsBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
        else -> DummyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MusicFeedViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<BaseMusicFeedRecycleItem>() {

            override fun areItemsTheSame(
                old: BaseMusicFeedRecycleItem,
                new: BaseMusicFeedRecycleItem
            ): Boolean {
                return old.id == new.id
            }

            override fun areContentsTheSame(
                old: BaseMusicFeedRecycleItem,
                new: BaseMusicFeedRecycleItem
            ): Boolean = old == new
        }
    }
}