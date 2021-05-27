package ru.spbstu.musicservice.ui.songs.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.spbstu.commons.SingleLiveData
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.songs.SongClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import javax.inject.Inject

@HiltViewModel
class SongsSearchViewModel @Inject constructor(
    databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _items = MediatorLiveData<State<PagedList<SongItem>>>()
    val items: LiveData<State<PagedList<SongItem>>>
        get() = _items

    private val _songEvent = SingleLiveData<Song>()
    val songEvent: LiveData<Song>
        get() = _songEvent

    private val dataSourceFactory = SongsSearchDataSourceFactory(databaseRepository, object : SongClickListener {
        override fun onSongClick(song: Song) {
            _songEvent.postValue(song)
        }
    })
    private val songs: LiveData<PagedList<SongItem>>

    init {
        val pagedConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        songs = LivePagedListBuilder(dataSourceFactory, pagedConfig).build()
        _items.addSource(songs) { result ->
            if (result.isEmpty()) {
                _items.postValue(State.Error(NoSuchElementException()))
            } else {
                _items.postValue(State.Success(result))
            }
        }
        _items.value = State.Loading()
    }

    fun searchSongs(query: String) {
        dataSourceFactory.query = query
        songs.value?.dataSource?.invalidate()
    }
}