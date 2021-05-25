package ru.spbstu.musicservice.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<State<List<SongItem>>>()
    val items: LiveData<State<List<SongItem>>>
        get() = _items

    fun loadPlaylistSongs(playlist: Playlist) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<SongItem>()
                    databaseRepository.getPlaylistSongs(playlist, 100)
                        .map {
                            SongItem(it, this@PlaylistViewModel)
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
}