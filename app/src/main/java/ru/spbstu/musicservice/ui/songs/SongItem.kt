package ru.spbstu.musicservice.ui.songs

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.commons.setTextOrHide
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.databinding.ItemSongBinding

class SongItem(
    val item: Song,
    private val songClickListener: SongClickListener,
) : BaseAdapterItem<SongItemViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_song

    override fun createViewHolder(parent: ViewGroup): SongItemViewHolder {
        return SongItemViewHolder(
            ItemSongBinding.inflate(parent.layoutInflater, parent, false),
            songClickListener
        )
    }

    override fun bind(holder: SongItemViewHolder) {
        holder.bind(item)
    }
}

class SongItemViewHolder(
    private val binding: ItemSongBinding,
    private val songClickListener: SongClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Song) {
        binding.tvSongName.setTextOrHide(item.name)
        binding.tvSongArtist.setTextOrHide(item.artist?.name)
        binding.tvSongLength.setTextOrHide(item.stringLength)
        binding.ivCover.setImageURI(item.imageUrl)
        binding.btnMore.setOnClickListener {
            songClickListener.onMoreButtonClick(it, item)
        }
        itemView.setOnClickListener {
            songClickListener.onSongClick(item)
        }
    }
}