package ru.spbstu.musicservice.ui.cds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.commons.SingleLiveData
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.NavigationEvent
import ru.spbstu.musicservice.ui.feed.NavigationType
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem
import javax.inject.Inject

@HiltViewModel
class CdsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<State<List<CdItem>>>()
    val items: LiveData<State<List<CdItem>>>
        get() = _items

    private val _navigationEvent = SingleLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent>
        get() = _navigationEvent

    init {
        loadCds()
    }

    private fun loadCds() {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<CdItem>()
                    databaseRepository.getCds(100)
                        .map {
                            CdItem(
                                BaseMusicFeedRecycleItem(
                                    R.id.view_type_music_feed_cds,
                                    it.id,
                                    it.name,
                                    rating = it.rating,
                                    entity = it
                                ), this@CdsViewModel
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

    fun onRefresh() {
        loadCds()
    }

    override fun onCdClick(item: BaseMusicFeedRecycleItem) {
        _navigationEvent.value = NavigationEvent(item, NavigationType.CD)
    }
}