package ru.spbstu.musicservice.ui.playlists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment.Companion.PARAM_PLAYLIST
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs

@AndroidEntryPoint
class PlaylistFragment : FragmentWithSongs() {

    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playlist: Playlist

    override fun getName(): String {
        return playlist.name
    }

    override fun getType(): String = getString(R.string.playlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getSerializable(PARAM_PLAYLIST) as Playlist

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageURI(playlist.imageUrl + "/?blur=7")
        if (savedInstanceState == null) {
            viewModel.loadPlaylistSongs(playlist)
        }
        viewModel.items.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showLoading()
                is State.Success -> {
                    showData()
                    adapter.submitList(it.item as List<BaseAdapterItem<RecyclerView.ViewHolder>>)
                }
                is State.Error -> showError(getString(R.string.playlist_no_songs))
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.onRefresh(playlist)
    }
}