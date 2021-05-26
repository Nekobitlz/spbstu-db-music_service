package ru.spbstu.musicservice.ui.songs.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.commons.SingleLiveData
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.songs.SongClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import ru.spbstu.musicservice.ui.songs.SongParams
import javax.inject.Inject

@HiltViewModel
class SongsSearchViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _items = MutableLiveData<State<List<SongItem>>>()
    val items: LiveData<State<List<SongItem>>>
        get() = _items

    private val _songEvent = SingleLiveData<Song>()
    val songEvent: LiveData<Song>
        get() = _songEvent

    fun searchSongs(query: String) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<SongItem>()
                    databaseRepository.searchSongs(query, 100)
                        .map {
                            SongItem(it, object : SongClickListener {
                                override fun onSongClick(song: Song) {
                                    _songEvent.postValue(song)
                                }
                            }, SongParams(showBtnMore = false, showPosition = false))
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
}