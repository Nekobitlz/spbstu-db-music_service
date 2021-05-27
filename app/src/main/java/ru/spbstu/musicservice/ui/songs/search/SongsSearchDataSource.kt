package ru.spbstu.musicservice.ui.songs.search

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.songs.SongClickListener
import ru.spbstu.musicservice.ui.songs.SongItem
import ru.spbstu.musicservice.ui.songs.SongParams

class SongsSearchDataSourceFactory(
    private val databaseRepository: DatabaseRepository,
    private val songClickListener: SongClickListener,
): DataSource.Factory<Int, SongItem>() {

    var query: String = ""

    override fun create(): DataSource<Int, SongItem> {
        return SongsSearchDataSource(query, databaseRepository, songClickListener)
    }
}

class SongsSearchDataSource(
    private val query: String,
    private val databaseRepository: DatabaseRepository,
    private val songClickListener: SongClickListener,
) : PositionalDataSource<SongItem>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<SongItem>) {
        val list = databaseRepository.searchSongs(query, params.requestedLoadSize).map {
            SongItem(it, songClickListener, SongParams(showBtnMore = false, showPosition = false))
        }
        Log.d("DATASOURCE_TAG", "initial $query ${params.requestedLoadSize} ${list.size}")
        callback.onResult(list, list.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SongItem>) {
        val list = databaseRepository.searchSongs(query, params.loadSize, params.startPosition).map {
            SongItem(it, songClickListener, SongParams(showBtnMore = false, showPosition = false))
        }
        Log.d("DATASOURCE_TAG", "range $query ${params.loadSize} ${params.startPosition} ${list.size}")
        callback.onResult(list)
    }
}