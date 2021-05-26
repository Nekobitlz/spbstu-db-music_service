package ru.spbstu.musicservice.ui.songs

import android.annotation.SuppressLint
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.Song

class SongPopupMenuController(
    private val callBack: MenuBuilder.Callback,
) {

    lateinit var song: Song
        private set
    lateinit var playlist: Playlist
        private set

    @SuppressLint("RestrictedApi")
    fun show(view: View, song: Song, playlist: Playlist) {
        this.song = song
        this.playlist = playlist
        val context = view.context
        MenuBuilder(context).apply {
            MenuInflater(context).inflate(R.menu.menu_song_item_options, this)
            setCallback(callBack)

            MenuPopupHelper(context, this, view).apply {
                setForceShowIcon(true)
                show()
            }
        }
    }
}