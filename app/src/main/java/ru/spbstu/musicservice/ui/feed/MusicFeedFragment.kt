package ru.spbstu.musicservice.ui.feed

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.*
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedClickListener
import ru.spbstu.musicservice.ui.feed.item.BaseMusicFeedRecycleItem

@AndroidEntryPoint
class MusicFeedFragment : BaseRecyclerFragment() {

    private val viewModel: MusicFeedViewModel by viewModels()

    override val adapter: BaseAdapter by lazyUnsychronized {
        createRecyclerAdapter() as BaseAdapter
    }

    override fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return BaseAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getSerializable(PARAM_USER)

        viewModel.items.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    recyclerBinding.emptyView.state = EmptyViewState.Loading
                    recyclerBinding.list.gone()
                }
                is State.Success -> {
                    recyclerBinding.emptyView.state = EmptyViewState.None
                    recyclerBinding.list.visible()
                    adapter.submitList(it.item)
                }
                is State.Error -> {
                    recyclerBinding.emptyView.state = EmptyViewState.Error()
                    recyclerBinding.list.gone()
                }
            }
        })
    }

    companion object {
        const val PARAM_USER = "PARAM_USER"
    }
}