package ru.spbstu.musicservice.ui.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.musicservice.data.Cd
import ru.spbstu.musicservice.data.Chart
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<State<List<SongItem>>>()
    val items: LiveData<State<List<SongItem>>>
        get() = _items

    fun loadChart(chart: Chart) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
             //   try {
                    val list = mutableListOf<SongItem>()
                    databaseRepository.getChartSongs(chart, 100)
                        .map {
                            SongItem(it, this@ChartViewModel)
                        }.also {
                            list.addAll(it)
                        }
                    if (list.isEmpty()) {
                        _items.postValue(State.Error(NoSuchElementException()))
                    } else {
                        _items.postValue(State.Success(list.toList()))
                    }
                /*} catch (t: Throwable) {
                    _items.postValue(State.Error(t))
                }*/
            }
        }
    }

    fun onRefresh(chart: Chart) {
        loadChart(chart)
    }
}