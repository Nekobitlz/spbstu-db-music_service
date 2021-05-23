package ru.spbstu.musicservice.ui.feed

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.*
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import javax.inject.Inject

@AndroidEntryPoint
class MusicFeedFragment : BaseRecyclerFragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: MusicFeedViewModel by viewModels()

    override val adapter: BaseAdapter by lazyUnsychronized {
        createRecyclerAdapter() as BaseAdapter
    }

    private lateinit var user: User

    override fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return BaseAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getSerializable(PARAM_USER) as User
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            viewModel.loadMusicFeeds(user)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_music_feed, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_user_settings) {
            navigator.toUserInfo(user)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setCustomView(R.layout.toolbar_music_feed_user)
            it.customView?.findViewById<TextView>(R.id.tv_user_name)?.apply {
                text = "${user.firstName} ${user.secondName}"
            }
            it.customView?.findViewById<SimpleDraweeView>(R.id.iv_avatar)?.apply {
                setActualImageResource(if (user.gender.id == "2") R.drawable.female else R.drawable.male)
            }
            it.customView?.setOnClickListener {
                Toast.makeText(context, "Click on toolbar", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.items.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    recyclerBinding.emptyView.state = EmptyViewState.Loading
                    recyclerBinding.list.gone()
                }
                is State.Success -> {
                    recyclerBinding.swipeRefresh.isRefreshing = false
                    recyclerBinding.emptyView.state = EmptyViewState.None
                    recyclerBinding.list.visible()
                    adapter.submitList(it.item)
                }
                is State.Error -> {
                    recyclerBinding.swipeRefresh.isRefreshing = false
                    recyclerBinding.emptyView.state = EmptyViewState.Error()
                    recyclerBinding.list.gone()
                }
            }
        })
    }

    override fun onRefresh() {
        viewModel.onRefresh(user)
    }

    companion object {
        const val PARAM_USER = "PARAM_USER"
    }
}