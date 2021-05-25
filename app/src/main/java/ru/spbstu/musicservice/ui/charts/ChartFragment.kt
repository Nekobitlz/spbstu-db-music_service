package ru.spbstu.musicservice.ui.charts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Chart
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs

@AndroidEntryPoint
class ChartFragment : FragmentWithSongs() {

    private val viewModel: ChartViewModel by viewModels()
    private lateinit var chart: Chart

    override fun getName(): String {
        return chart.name
    }

    override fun getType(): String = getString(R.string.chart)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chart = arguments?.getSerializable(MusicFeedFragment.PARAM_CHART) as Chart

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageURI(chart.imageUrl + "/?blur=7")
        if (savedInstanceState == null) {
            viewModel.loadChart(chart)
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
        viewModel.onRefresh(chart)
    }
}