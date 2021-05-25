package ru.spbstu.musicservice.ui.cds

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Cd
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs

@AndroidEntryPoint
class CdFragment : FragmentWithSongs() {

    private val viewModel: CdViewModel by viewModels()
    private lateinit var cd: Cd

    override fun getName(): String {
        return cd.name
    }

    override fun getType(): String = getString(R.string.cd)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cd = arguments?.getSerializable(MusicFeedFragment.PARAM_CD) as Cd

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageURI(cd.imageUrl + "/?blur=7")
        if (savedInstanceState == null) {
            viewModel.loadCdSong(cd)
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
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.onRefresh(cd)
    }
}