package ru.spbstu.musicservice.ui.songs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.spbstu.commons.*
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.FragmentWithSongsBinding

abstract class FragmentWithSongs : Fragment(R.layout.fragment_with_songs) {

    protected val binding: FragmentWithSongsBinding by viewBinding()
    protected val adapter = BaseAdapter(object : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.item == newItem.item
        }
    } as DiffUtil.ItemCallback<BaseAdapterItem<RecyclerView.ViewHolder>>)

    abstract fun getName(): String
    abstract fun getType(): String

    override fun onStart() {
        super.onStart()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        binding.recyclerView.addItemDecoration(
            DividerItemDecorator(requireContext())
        )
        binding.collapsingToolbar.title = getName()
        binding.tvName.text = getType()
        binding.toolbar.setNavigationIcon(R.drawable.md_nav_back)
        binding.toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.isNestedScrollingEnabled = true
    }

    protected open fun onRefresh() {
    }

    protected open fun showData() {
        binding.emptyView.state = EmptyViewState.None
        binding.recyclerView.visible()
    }

    protected open fun showError(
        text: String = resources.getString(ru.spbstu.commons.R.string.default_error),
        onRetryClick: (() -> Unit)? = { onRefresh() },
    ) {
        binding.emptyView.state = EmptyViewState.Error(text, onRetryClick)
        binding.recyclerView.gone()
    }

    protected open fun showLoading() {
        binding.emptyView.state = EmptyViewState.Loading
        binding.recyclerView.gone()
    }
}