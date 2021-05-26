package ru.spbstu.musicservice.ui.songs

import android.view.View
import ru.spbstu.musicservice.data.Song

interface SongClickListener {

    fun onSongClick(song: Song)

    fun onMoreButtonClick(view: View, song: Song) {}
}