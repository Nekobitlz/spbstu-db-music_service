package ru.spbstu.musicservice.ui.feed.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedPagerBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedTitleBinding
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

class PagerMusicFeedItem(
    private val list: List<BaseMusicFeedRecycleItem>,
    private val musicFeedClickListener: MusicFeedClickListener
) : BaseAdapterItem<PagerViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_pager

    override fun createViewHolder(parent: ViewGroup): PagerViewHolder {
        return PagerViewHolder(
            ItemMusicFeedPagerBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
    }

    override fun bind(holder: PagerViewHolder) {
        holder.bind(list)
    }
}

class PagerViewHolder(
    binding: ItemMusicFeedPagerBinding,
    musicFeedClickListener: MusicFeedClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val recyclerView = binding.pager
    private val adapter = PagerAdapter(musicFeedClickListener).also {
        recyclerView.adapter = it
    }

    fun bind(list: List<BaseMusicFeedRecycleItem>) {
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            itemView.context, RecyclerView.HORIZONTAL, false
        )
        recyclerView.setHasFixedSize(true)
        adapter.submitList(list)
    }
}