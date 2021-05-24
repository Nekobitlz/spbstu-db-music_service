package ru.spbstu.musicservice.ui.songs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.FragmentWithSongsBinding


abstract class FragmentWithSongs : Fragment(R.layout.fragment_with_songs) {

    protected val binding: FragmentWithSongsBinding by viewBinding()

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
        binding.collapsingToolbar.title = getName()
        binding.tvName.text = getType()
        binding.toolbar.setNavigationIcon(R.drawable.md_nav_back)
        binding.toolbar.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }
}