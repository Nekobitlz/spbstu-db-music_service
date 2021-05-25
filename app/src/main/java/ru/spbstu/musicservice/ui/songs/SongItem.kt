package ru.spbstu.musicservice.ui.songs

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.commons.setTextOrHide
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.databinding.ItemSongBinding
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener

class SongItem(
    private val item: Song,
    private val musicFeedClickListener: MusicFeedClickListener,
) : BaseAdapterItem<SongItemViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_song

    override fun createViewHolder(parent: ViewGroup): SongItemViewHolder {
        return SongItemViewHolder(
            ItemSongBinding.inflate(parent.layoutInflater, parent, false),
            musicFeedClickListener
        )
    }

    override fun bind(holder: SongItemViewHolder) {
        holder.bind(item)
    }
}

class SongItemViewHolder(
    private val binding: ItemSongBinding,
    private val musicFeedClickListener: MusicFeedClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Song) {
        binding.tvSongName.setTextOrHide(item.name)
        binding.tvSongArtist.setTextOrHide(item.artist?.name)
        binding.tvSongLength.setTextOrHide(item.stringLength)
        binding.ivCover.setImageURI(item.imageUrl)
        itemView.setOnClickListener {
            musicFeedClickListener.onSongClick(item)
        }
    }
}