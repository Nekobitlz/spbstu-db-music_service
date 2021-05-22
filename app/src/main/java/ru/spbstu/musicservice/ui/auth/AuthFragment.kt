package ru.spbstu.musicservice.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.commons.hide
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.FragmentAuthBinding
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentAuthBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.hide()
        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSubmit.hide()
                }
                is State.Success -> {
                    binding.progressBar.hide()
                    binding.btnSubmit.visible()
                    navigator.navigateTo(
                        MusicFeedFragment(),
                        Bundle().apply {
                            putSerializable(MusicFeedFragment.PARAM_USER, it.item)
                        }
                    )
                }
                is State.Error -> {
                    binding.progressBar.hide()
                    binding.btnSubmit.visible()
                    MaterialDialog(context ?: return@observe)
                        .message(R.string.login_error)
                        .positiveButton(R.string.ok)
                        .show()
                }
            }
        }
        binding.btnSubmit.setOnClickListener {
            viewModel.onSubmitClick(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }
}