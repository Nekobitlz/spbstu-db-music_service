package ru.spbstu.musicservice.ui.playlists

import android.os.Bundle
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment.Companion.PARAM_PLAYLIST
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs

class PlaylistFragment : FragmentWithSongs() {

    private lateinit var playlist: Playlist

    override fun getName(): String {
        return playlist.name
    }

    override fun getType(): String = getString(R.string.playlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getSerializable(PARAM_PLAYLIST) as Playlist
    }
}