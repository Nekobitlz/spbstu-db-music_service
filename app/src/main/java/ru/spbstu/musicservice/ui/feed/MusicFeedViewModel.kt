package ru.spbstu.musicservice.ui.feed

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.adapter.PagerMusicFeedItem
import ru.spbstu.musicservice.ui.feed.adapter.TitleMusicFeedItem
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem
import javax.inject.Inject

@HiltViewModel
class MusicFeedViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    @ApplicationContext private val context: Context,
) : ViewModel(), MusicFeedClickListener {

    private val _items = MutableLiveData<List<BaseAdapterItem<RecyclerView.ViewHolder>>>()
    val items: LiveData<List<BaseAdapterItem<RecyclerView.ViewHolder>>>
        get() = _items

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = mutableListOf<BaseAdapterItem<out RecyclerView.ViewHolder>>()
                databaseRepository.getCd(10)
                    .map {
                        BaseMusicFeedRecycleItem(
                            R.id.view_type_music_feed_cds,
                            it.id,
                            it.name,
                            rating = it.rating
                        )
                    }
                    .also {
                        list += TitleMusicFeedItem(R.string.best_cd)
                        list += PagerMusicFeedItem(it, this@MusicFeedViewModel)
                    }
                _items.postValue(list.toList() as List<BaseAdapterItem<RecyclerView.ViewHolder>>)
            }
        }
    }

    override fun onChartClick(item: BaseMusicFeedRecycleItem) {
        Toast.makeText(context, "Click on ${item.title} chart", Toast.LENGTH_SHORT).show()
    }

    override fun onCdClick(item: BaseMusicFeedRecycleItem) {
        Toast.makeText(context, "Click on ${item.title} cd", Toast.LENGTH_SHORT).show()
    }

    override fun onPlaylistClick(item: BaseMusicFeedRecycleItem) {
        Toast.makeText(context, "Click on ${item.title} playlist", Toast.LENGTH_SHORT).show()
    }
}