package ru.spbstu.musicservice.ui.songs.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.spbstu.commons.BaseRecyclerFragment
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.adapter.BasePagedAdapter
import ru.spbstu.commons.lazyUnsychronized
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.playlists.PlaylistFragment.Companion.PARAM_SEARCH_REQUEST
import ru.spbstu.musicservice.ui.playlists.PlaylistFragment.Companion.PARAM_SONG
import javax.inject.Inject

@AndroidEntryPoint
class SongsSearchFragment : BaseRecyclerFragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: SongsSearchViewModel by viewModels()

    override val adapter: BasePagedAdapter by lazyUnsychronized {
        createRecyclerAdapter() as BasePagedAdapter
    }

    override fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return BasePagedAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.items.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showLoading()
                is State.Success -> {
                    showData()
                    adapter.submitList(it.item as PagedList<BaseAdapterItem<RecyclerView.ViewHolder>>)
                }
                is State.Error -> showError("По вашему запросу ничего не найдено", null)
            }
        }
        viewModel.songEvent.observe(viewLifecycleOwner) {
            if (arguments?.getBoolean(PARAM_SHOULD_RETURN_RESULT) == true) {
                setFragmentResult(
                    PARAM_SEARCH_REQUEST,
                    Bundle().apply { putSerializable(PARAM_SONG, it) }
                )
                activity?.onBackPressed()
            } else {
                navigator.toSong(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        menuItem.expandActionView()
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean = true

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                activity?.onBackPressed()
                return true
            }

        })

        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.songs_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var debounceJob: Job? = null

            override fun onQueryTextSubmit(text: String): Boolean = true

            override fun onQueryTextChange(text: String): Boolean {
                debounceJob?.cancel()
                debounceJob = lifecycleScope.launch {
                    delay(300L)
                    viewModel.searchSongs(text)
                }
                return true
            }
        })
    }

    companion object {
        const val PARAM_SHOULD_RETURN_RESULT: String = "PARAM_SHOULD_RETURN_RESULT"
    }
}