package ru.spbstu.musicservice.ui.feed

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import com.facebook.drawee.view.SimpleDraweeView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.*
import ru.spbstu.commons.adapter.BaseAdapter
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Cd
import ru.spbstu.musicservice.data.Chart
import ru.spbstu.musicservice.data.Playlist
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.adapter.MusicFeedPlaylistsViewHolder
import ru.spbstu.musicservice.ui.main.APP_STORAGE
import ru.spbstu.musicservice.ui.main.PARAM_AUTH_USERS_COUNT
import ru.spbstu.musicservice.ui.playlists.PlaylistFragment
import ru.spbstu.musicservice.ui.playlists.PlaylistsFragment
import ru.spbstu.musicservice.ui.songs.FragmentWithSongs
import javax.inject.Inject

@AndroidEntryPoint
class MusicFeedFragment : BaseRecyclerFragment() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: MusicFeedViewModel by viewModels()

    override val adapter: BaseAdapter by lazyUnsychronized {
        createRecyclerAdapter() as BaseAdapter
    }

    override fun createRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return BaseAdapter()
    }

    private lateinit var user: User

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
        activity?.supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            actionBar.setCustomView(R.layout.toolbar_music_feed_user)
            actionBar.customView?.findViewById<TextView>(R.id.tv_user_name)?.apply {
                text = "${user.firstName} ${user.secondName}"
            }
            actionBar.customView?.findViewById<SimpleDraweeView>(R.id.iv_avatar)?.apply {
                setActualImageResource(if (user.gender.id == "2") R.drawable.female else R.drawable.male)
            }
            actionBar.customView?.setOnClickListener {
                MaterialDialog(it.context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    cornerRadius(16f)
                    title(R.string.choose_account)
                    val sharedPreferences =
                        activity.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
                    val usersCount = sharedPreferences?.getInt(PARAM_AUTH_USERS_COUNT, 0) ?: 0
                    val list = mutableListOf<User>()
                    for (i in 0 until usersCount) {
                        val json = sharedPreferences.getString(PARAM_USER + i, null) ?: continue
                        val user = Gson().fromJson(json, User::class.java)
                        list.add(user)
                    }
                    val items = list
                        .map { "${it.firstName} ${it.secondName}" }
                        .toMutableList()
                        .apply { add(resources.getString(R.string.add_user)) }
                    listItems(items = items) { dialog, index, text ->
                        if (items.size - 1 == index) {
                            navigator.toAuth(true)
                        } else {
                            navigator.toMusicFeed(list[index], true)
                        }
                    }
                }
            }
        }
        viewModel.items.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> showLoading()
                is State.Success -> {
                    showData()
                    adapter.submitList(it.item)
                }
                is State.Error -> showError()
            }
        })
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            when (it.type) {
                NavigationType.CHART -> navigator.toChart(it.item?.entity as? Chart ?: return@observe)
                NavigationType.CD -> navigator.toCd(it.item?.entity as? Cd ?: return@observe)
                NavigationType.PLAYLIST -> navigator.toPlaylist(it.item?.entity as? Playlist ?: return@observe)
                NavigationType.CHART_MORE -> navigator.toCharts()
                NavigationType.CD_MORE -> navigator.toCds()
                NavigationType.PLAYLIST_MORE -> navigator.toPlaylists(user)
            }
        }
    }

    override fun onRefresh() {
        viewModel.onRefresh(user)
    }

    companion object {
        const val PARAM_CHART = "PARAM_CHART"
        const val PARAM_CD = "PARAM_CD"
        const val PARAM_USER = "PARAM_USER"
        const val PARAM_PLAYLIST = "PARAM_PLAYLIST"
    }
}