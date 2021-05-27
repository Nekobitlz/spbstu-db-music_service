package ru.spbstu.musicservice.ui.feed.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.commons.SingleLiveData
import ru.spbstu.musicservice.data.Artist
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.songs.SongClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import ru.spbstu.musicservice.ui.songs.SongParams
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), SongClickListener {

    private val _items = MutableLiveData<State<List<SongItem>>>()
    val items: LiveData<State<List<SongItem>>>
        get() = _items

    private val _songEvent = SingleLiveData<Song>()
    val songEvent: LiveData<Song>
        get() = _songEvent

    fun loadArtist(artist: Artist) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
               // try {
                    val list = mutableListOf<SongItem>()
                    databaseRepository.getArtistSongs(artist, 100)
                        .map {
                            SongItem(it, this@ArtistViewModel, SongParams(showBtnMore = false, showPosition = false))
                        }.also {
                            list.addAll(it)
                        }
                    if (list.isEmpty()) {
                        _items.postValue(State.Error(NoSuchElementException()))
                    } else {
                        _items.postValue(State.Success(list.toList()))
                    }
               /* } catch (t: Throwable) {
                    _items.postValue(State.Error(t))
                }*/
            }
        }
    }

    fun onRefresh(artist: Artist) {
        loadArtist(artist)
    }

    override fun onSongClick(song: Song) {
        _songEvent.postValue(song)
    }
}