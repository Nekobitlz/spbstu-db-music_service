package ru.spbstu.musicservice.ui.feed.adapter

import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.DimenUtils
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.ItemMusicFeedTitleBinding

class TitleMusicFeedItem(
    @StringRes private val titleRes: Int,
    @DimenRes private val topPadding: Int? = null,
    private val onClick: () -> Unit,
) : BaseAdapterItem<TitleMusicFeedViewHolder>() {

    override val viewType: Int
        get() = R.id.view_type_music_feed_title

    override fun createViewHolder(parent: ViewGroup): TitleMusicFeedViewHolder {
        return TitleMusicFeedViewHolder(
            ItemMusicFeedTitleBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun bind(holder: TitleMusicFeedViewHolder) {
        holder.bind(titleRes, topPadding, onClick)
    }

}

class TitleMusicFeedViewHolder(
    binding: ItemMusicFeedTitleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val tvTitle = binding.tvTitle

    fun bind(
        @StringRes titleRes: Int,
        @DimenRes topPadding: Int?,
        onClick: () -> Unit,
    ) {
        val resources = itemView.resources
        tvTitle.text = resources.getString(titleRes)
        if (topPadding != null) {
            (tvTitle.layoutParams as? ViewGroup.MarginLayoutParams)!!.updateMargins(
                top = DimenUtils.dimenResourceToPixels(
                    itemView.context,
                    topPadding
                )
            )
        }
        itemView.setOnClickListener {
            onClick()
        }
    }
}