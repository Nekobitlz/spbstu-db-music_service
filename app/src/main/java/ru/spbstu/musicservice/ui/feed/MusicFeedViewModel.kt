package ru.spbstu.musicservice.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.adapter.PagerMusicFeedItem
import ru.spbstu.musicservice.ui.feed.adapter.TitleMusicFeedItem
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem
import javax.inject.Inject

@HiltViewModel
class MusicFeedViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<State<List<BaseAdapterItem<RecyclerView.ViewHolder>>>>()
    val items: LiveData<State<List<BaseAdapterItem<RecyclerView.ViewHolder>>>>
        get() = _items

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent>
        get() = _navigationEvent

    fun loadMusicFeeds(user: User) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<BaseAdapterItem<out RecyclerView.ViewHolder>>()
                    databaseRepository.getCds(10)
                        .map {
                            BaseMusicFeedRecycleItem(
                                R.id.view_type_music_feed_cds,
                                it.id,
                                it.name,
                                rating = it.rating
                            )
                        }
                        .also {
                            if (it.isNotEmpty()) {
                                list += TitleMusicFeedItem(
                                    R.string.best_cd,
                                    onClick = {
                                        _navigationEvent.value = NavigationEvent(type = NavigationType.CD_MORE)
                                    })
                                list += PagerMusicFeedItem(it, this@MusicFeedViewModel)
                            }
                        }
                    databaseRepository.getCharts(10)
                        .map {
                            BaseMusicFeedRecycleItem(
                                R.id.view_type_music_feed_charts,
                                it.id,
                                it.name
                            )
                        }
                        .also {
                            if (it.isNotEmpty()) {
                                list += TitleMusicFeedItem(
                                    R.string.best_charts,
                                    R.dimen.title_spacing_12,
                                    onClick = {
                                        _navigationEvent.value = NavigationEvent(type = NavigationType.CHART_MORE)
                                    }
                                )
                                list += PagerMusicFeedItem(it, this@MusicFeedViewModel)
                            }
                        }
                    databaseRepository.getPlaylists(user, 10)
                        .map {
                            BaseMusicFeedRecycleItem(
                                R.id.view_type_music_feed_playlists,
                                it.id,
                                it.name
                            )
                        }
                        .also {
                            if (it.isNotEmpty()) {
                                list += TitleMusicFeedItem(
                                    R.string.my_playlists,
                                    R.dimen.title_spacing_12,
                                    onClick = {
                                        _navigationEvent.value = NavigationEvent(type = NavigationType.PLAYLIST_MORE)
                                    }
                                )
                                list += PagerMusicFeedItem(it, this@MusicFeedViewModel)
                            }
                        }
                    if (list.isEmpty()) {
                        _items.postValue(State.Error(NoSuchElementException()))
                    } else {
                        _items.postValue(State.Success(list.toList() as List<BaseAdapterItem<RecyclerView.ViewHolder>>))
                    }
                } catch (t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    _items.postValue(State.Error(t))
                }
            }
        }
    }

    override fun onChartClick(item: BaseMusicFeedRecycleItem) {
        _navigationEvent.value = NavigationEvent(item, NavigationType.CHART)
    }

    override fun onCdClick(item: BaseMusicFeedRecycleItem) {
        _navigationEvent.value = NavigationEvent(item, NavigationType.CD)
    }

    override fun onPlaylistClick(item: BaseMusicFeedRecycleItem) {
        _navigationEvent.value = NavigationEvent(item, NavigationType.PLAYLIST)
    }

    fun onRefresh(user: User) {
        loadMusicFeeds(user)
    }
}

class NavigationEvent(
    val item: BaseMusicFeedRecycleItem? = null,
    val type: NavigationType = NavigationType.NONE
)

enum class NavigationType {
    NONE,
    CHART, CD, PLAYLIST,
    CHART_MORE, CD_MORE, PLAYLIST_MORE,
}