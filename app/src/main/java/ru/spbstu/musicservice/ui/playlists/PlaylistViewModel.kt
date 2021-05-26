package ru.spbstu.musicservice.ui.playlists

import android.annotation.SuppressLint
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.songs.SongClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import ru.spbstu.musicservice.ui.songs.SongPopupMenuController
import javax.inject.Inject

@SuppressLint("RestrictedApi")
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    @ApplicationContext private val context: Context,
) : ViewModel(), MenuBuilder.Callback {

    private val popupMenuController = SongPopupMenuController(this)

    private val _items = MutableLiveData<State<List<SongItem>>>()
    val items: LiveData<State<List<SongItem>>>
        get() = _items

    fun loadPlaylistSongs(playlist: Playlist, shouldLoading: Boolean = true) {
        viewModelScope.launch {
            if (shouldLoading) _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<SongItem>()
                    databaseRepository.getPlaylistSongs(playlist, 100)
                        .map {
                            SongItem(it, object : SongClickListener {
                                override fun onSongClick(song: Song) {
                                    TODO("Not yet implemented")
                                }

                                override fun onMoreButtonClick(view: View, song: Song) {
                                    popupMenuController.show(view, song, playlist)
                                }
                            })
                        }.also {
                            list.addAll(it)
                        }
                    if (list.isEmpty()) {
                        _items.postValue(State.Error(NoSuchElementException()))
                    } else {
                        _items.postValue(State.Success(list.toList()))
                    }
                } catch (t: Throwable) {
                    _items.postValue(State.Error(t))
                }
            }
        }
    }

    fun onRefresh(playlist: Playlist) {
        loadPlaylistSongs(playlist)
    }

    override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_remove_song -> {
                removeSong()
                return true
            }
        }
        return false
    }

    override fun onMenuModeChange(menu: MenuBuilder) {}

    private fun removeSong() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val playlist = popupMenuController.playlist
                    val song = popupMenuController.song
                    val isSuccess = databaseRepository.removeSong(playlist, song)
                    withContext(Dispatchers.Main) {
                        val data = _items.value
                        if (isSuccess && data is State.Success) {
                            loadPlaylistSongs(playlist, false)
                        } else {
                            showErrorToast("Не удалось удалить песню из плейлиста")
                        }
                    }
                } catch (t: Throwable) {
                    withContext(Dispatchers.Main) {
                        showErrorToast("Не удалось удалить песню из плейлиста")
                    }
                }
            }
        }
    }

    private fun showErrorToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}