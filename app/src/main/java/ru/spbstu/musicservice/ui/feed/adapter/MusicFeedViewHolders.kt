package ru.spbstu.musicservice.ui.feed.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.spbstu.commons.setTextOrHide
import ru.spbstu.musicservice.databinding.ItemMusicFeedCdsBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedChartsBinding
import ru.spbstu.musicservice.databinding.ItemMusicFeedPlaylistsBinding
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

abstract class MusicFeedViewHolder(
    viewBinding: ViewBinding,
    protected val musicFeedClickListener: MusicFeedClickListener
) : RecyclerView.ViewHolder(viewBinding.root) {

    abstract fun bind(item: BaseMusicFeedRecycleItem)
}

class MusicFeedPlaylistsViewHolder(
    viewBinding: ItemMusicFeedPlaylistsBinding,
    musicFeedClickListener: MusicFeedClickListener
) : MusicFeedViewHolder(viewBinding, musicFeedClickListener) {

    private val binding = viewBinding

    override fun bind(item: BaseMusicFeedRecycleItem) {
        binding.tvTitle.setTextOrHide(item.title)
        binding.ivCover.setImageURI(item.imageUrl)
        itemView.setOnClickListener {
            musicFeedClickListener.onPlaylistClick(item)
        }
    }
}

class MusicFeedChartsViewHolder(
    viewBinding: ItemMusicFeedChartsBinding,
    musicFeedClickListener: MusicFeedClickListener
) : MusicFeedViewHolder(viewBinding, musicFeedClickListener) {

    private val binding = viewBinding

    override fun bind(item: BaseMusicFeedRecycleItem) {
        binding.tvTitle.setTextOrHide(item.title)
        binding.ivCover.setImageURI(item.imageUrl)
        itemView.setOnClickListener {
            musicFeedClickListener.onPlaylistClick(item)
        }
    }
}

class MusicFeedCdsViewHolder(
    viewBinding: ItemMusicFeedCdsBinding,
    musicFeedClickListener: MusicFeedClickListener
) : MusicFeedViewHolder(viewBinding, musicFeedClickListener) {

    private val binding = viewBinding

    override fun bind(item: BaseMusicFeedRecycleItem) {
        binding.tvTitle.setTextOrHide(item.title)
        binding.tvRating.setTextOrHide(item.rating.toString())
        binding.ivCover.setImageURI(item.imageUrl)
        itemView.setOnClickListener {
            musicFeedClickListener.onPlaylistClick(item)
        }
    }
}