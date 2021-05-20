package ru.spbstu.musicservice.ui.feed.adapter

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedTitleBinding

class TitleMusicFeedItem(
    @StringRes private val titleRes: Int,
) : BaseAdapterItem<TitleMusicFeedViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_title

    override fun createViewHolder(parent: ViewGroup): TitleMusicFeedViewHolder {
        return TitleMusicFeedViewHolder(
            ItemMusicFeedTitleBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun bind(holder: TitleMusicFeedViewHolder) {
        holder.bind(titleRes)
    }

}

class TitleMusicFeedViewHolder(
    binding: ItemMusicFeedTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val tvTitle = binding.tvTitle

    fun bind(@StringRes titleRes: Int) {
        val resources = itemView.resources
        tvTitle.text = resources.getString(titleRes)
    }
}