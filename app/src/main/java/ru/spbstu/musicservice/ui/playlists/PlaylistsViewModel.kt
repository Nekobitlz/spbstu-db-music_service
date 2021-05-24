package ru.spbstu.musicservice.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<State<List<PlaylistItem>>>()
    val items: LiveData<State<List<PlaylistItem>>>
        get() = _items

    fun loadPlaylists(user: User) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<PlaylistItem>()
                    databaseRepository.getPlaylists(user, 100)
                        .map {
                            PlaylistItem(
                                BaseMusicFeedRecycleItem(
                                    R.id.view_type_music_feed_playlists,
                                    it.id,
                                    it.name
                                ), this@PlaylistsViewModel
                            )
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

    fun onRefresh(user: User) {
        loadPlaylists(user)
    }

    override fun onChartClick(item: BaseMusicFeedRecycleItem) {
        TODO("Not yet implemented")
    }

    override fun onCdClick(item: BaseMusicFeedRecycleItem) {
        TODO("Not yet implemented")
    }

    override fun onPlaylistClick(item: BaseMusicFeedRecycleItem) {
        TODO("Not yet implemented")
    }
}