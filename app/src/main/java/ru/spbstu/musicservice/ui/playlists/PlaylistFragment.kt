package ru.spbstu.musicservice.ui.playlists

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment.Companion.PARAM_PLAYLIST
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : FragmentWithSongs() {

    @Inject
    lateinit var navigator: Navigator

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
        binding.fab.visible()
        binding.fab.setOnClickListener {
            navigator.toSongsSearch(true)
        }
        binding.image.setImageURI(playlist.imageUrl + "/?blur=7")
        binding.toolbar.inflateMenu(R.menu.menu_playlist)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "musicservice.app/playlist/${playlist.id}")
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener false
        }
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
                is State.Error -> showError(getString(R.string.playlist_no_songs),
                    getString(R.string.add),
                    onRetryClick = {
                        navigator.toSongsSearch(true)
                    })
            }
        }
        parentFragmentManager.setFragmentResultListener(PARAM_SEARCH_REQUEST,
            this) { requestKey, bundle ->
            if (requestKey == PARAM_SEARCH_REQUEST) {
                viewModel.onSongSelected(playlist, bundle.getSerializable(PARAM_SONG) as Song)
            }
        }
        viewModel.songEvent.observe(viewLifecycleOwner, navigator::toSong)
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.onRefresh(playlist)
    }

    companion object {
        const val PARAM_SEARCH_REQUEST = "PARAM_SEARCH_REQUEST"
        const val PARAM_SONG = "PARAM_SONG"
    }
}