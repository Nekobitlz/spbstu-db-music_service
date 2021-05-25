package ru.spbstu.musicservice.ui.cds

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.BaseRecyclerFragment
import ru.spbstu.commons.DimenUtils
import ru.spbstu.commons.GridSpacingItemDecoration
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.lazyUnsychronized
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Cd
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.NavigationType
import javax.inject.Inject

@AndroidEntryPoint
class CdsFragment : BaseRecyclerFragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: CdsViewModel by viewModels()

    override val adapter: BaseAdapter by lazyUnsychronized {
        createRecyclerAdapter() as BaseAdapter
    }

    override fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return BaseAdapter()
    }

    override fun createRecyclerLayoutManager(): LinearLayoutManager {
        return GridLayoutManager(context, 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.let {
            it.setDisplayShowCustomEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = resources.getString(R.string.best_cd)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(DimenUtils.dpToPixels(context, 8f).toInt(), false)
        )
        viewModel.items.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> showLoading()
                is State.Success -> {
                    showData()
                    adapter.submitList(it.item as List<BaseAdapterItem<RecyclerView.ViewHolder>>)
                }
                is State.Error -> showError()
            }
        }
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            when (it.type) {
                NavigationType.CD -> navigator.toCd(it.item?.entity as? Cd ?: return@observe)
            }
        }
    }

    override fun onRefresh() {
        viewModel.onRefresh()
    }
}