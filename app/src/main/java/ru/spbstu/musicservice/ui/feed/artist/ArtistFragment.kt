package ru.spbstu.musicservice.ui.feed.artist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.DividerItemDecorator
import ru.spbstu.commons.EmptyViewState
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.gone
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Artist
import ru.spbstu.musicservice.databinding.FragmentArtistBinding
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.songs.SongItem
import javax.inject.Inject

@AndroidEntryPoint
class ArtistFragment : Fragment(R.layout.fragment_artist) {

    @Inject
    lateinit var navigator: Navigator

    protected val binding: FragmentArtistBinding by viewBinding()
    private val viewModel: ArtistViewModel by viewModels()

    protected val adapter = BaseAdapter(object : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return oldItem.item == newItem.item
        }
    } as DiffUtil.ItemCallback<BaseAdapterItem<RecyclerView.ViewHolder>>)

    private lateinit var artist: Artist

    fun getName(): String = artist.name
    fun getType(): String = getString(R.string.artist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artist = arguments?.getSerializable(PARAM_ARTIST) as Artist
    }

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
        binding.image.setImageURI(artist.imageUrl)
        binding.tvRole.text = getType()
        binding.tvRating.text = artist.rating.toString()
        binding.tvDescription.text = artist.description
        binding.toolbar.setNavigationIcon(R.drawable.md_nav_back)
        binding.toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.isNestedScrollingEnabled = true

        if (savedInstanceState == null) {
            viewModel.loadArtist(artist)
        }
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
        viewModel.songEvent.observe(viewLifecycleOwner, navigator::toSong)
    }

    protected fun onRefresh() {
        viewModel.onRefresh(artist)
    }

    protected fun showData() {
        binding.emptyView.state = EmptyViewState.None
        binding.recyclerView.visible()
    }

    protected fun showError(
        text: String = resources.getString(R.string.default_error),
        retryText: String = resources.getString(R.string.retry),
        onRetryClick: (() -> Unit)? = { onRefresh() },
    ) {
        binding.emptyView.state = EmptyViewState.Error(text, retryText, onRetryClick)
        binding.recyclerView.gone()
    }

    protected fun showLoading() {
        binding.emptyView.state = EmptyViewState.Loading
        binding.recyclerView.gone()
    }

    companion object {
        const val PARAM_ARTIST = "PARAM_ARTIST"
    }
}