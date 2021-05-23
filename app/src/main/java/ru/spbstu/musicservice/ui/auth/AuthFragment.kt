package ru.spbstu.musicservice.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import org.postgresql.util.Base64
import ru.spbstu.commons.hide
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.APP_STORAGE
import ru.spbstu.musicservice.PARAM_LOGIN_TOKEN
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
                    binding.etLogin.isEnabled = false
                    binding.etPassword.isEnabled = false
                }
                is State.Success -> {
                    saveToken()
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
                    binding.etLogin.isEnabled = true
                    binding.etPassword.isEnabled = true
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

    private fun saveToken() {
        val sharedPreferences = activity?.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
        val token = binding.etLogin.text.toString() + binding.etPassword.text.toString()
        val encodedToken = Base64.encodeBytes(token.encodeToByteArray())
        sharedPreferences?.edit()?.putString(PARAM_LOGIN_TOKEN, encodedToken)?.apply()
    }
}