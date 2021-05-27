package ru.spbstu.musicservice.ui.songs

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.DimenUtils
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Song
import ru.spbstu.musicservice.databinding.FragmentSongBinding
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.playlists.PlaylistFragment.Companion.PARAM_SONG
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentSongBinding by viewBinding()

    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        song = arguments?.getSerializable(PARAM_SONG) as Song
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(R.layout.fragment_song, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet = dialog.findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        ) ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvName.text = song.name
        binding.image.setImageURI(song.imageUrl)
        val artistTextView = TextView(context).apply {
            text = getString(R.string.song_info_artist) + " " + (song.artist?.name ?: getString(R.string.unknown_artist))
            textSize = 21f
            setTypeface(typeface, Typeface.BOLD)
            with(TypedValue()) {
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
                setBackgroundResource(resourceId)
            }
            setTextColor(ContextCompat.getColor(context, R.color.default_text))
            setOnClickListener {
                navigator.toArtist(song.artist ?: return@setOnClickListener)
                dismiss()
            }
            val padding_12 = DimenUtils.dpToPixels(context, 12f).toInt()
            val padding_8 = DimenUtils.dpToPixels(context, 8f).toInt()
            setPadding(padding_12, padding_8, padding_12, 0)
        }
        binding.container.addView(artistTextView)

        val list = listOf(
            getString(R.string.song_info_length) + " " + song.stringLength,
            getString(R.string.song_info_release_date) + " " + song.releaseDate,
            getString(R.string.song_info_genre) + " " + (song.genre?.name ?: "Популярный"),
            getString(R.string.song_info_playbacks) + " " + song.playbacksCount,
            getString(R.string.song_info_rating) + " " + song.rating
        )
        list.forEach {
            val textView = TextView(context).apply {
                text = it
                textSize = 17f
                setTextColor(ContextCompat.getColor(context, R.color.grey))
                val padding_12 = DimenUtils.dpToPixels(context, 12f).toInt()
                val padding_8 = DimenUtils.dpToPixels(context, 8f).toInt()
                setPadding(padding_12, padding_8, padding_12, 0)
            }
            binding.container.addView(textView)
        }
    }
}