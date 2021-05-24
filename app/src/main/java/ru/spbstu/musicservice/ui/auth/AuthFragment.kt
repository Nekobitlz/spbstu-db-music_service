package ru.spbstu.musicservice.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.postgresql.util.Base64
import ru.spbstu.commons.hide
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.databinding.FragmentAuthBinding
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import ru.spbstu.musicservice.ui.main.*
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    @Inject
    lateinit var navigator: Navigator

    private val binding: FragmentAuthBinding by viewBinding()
    private val viewModel: AuthViewModel by viewModels()

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
        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSubmit.hide()
                    binding.etLogin.isEnabled = false
                    binding.etPassword.isEnabled = false
                }
                is State.Success -> {
                    saveToken(it.item)
                    navigator.toMusicFeed(it.item, true)
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
        binding.btnRegister.setOnClickListener {
            navigator.toRegister()
        }
    }

    private fun saveToken(user: User) {
        val sharedPreferences = activity?.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)

        val loginText = binding.etLogin.text.toString()
        val encodedLogin = Base64.encodeBytes(loginText.encodeToByteArray())

        val passwordText = binding.etPassword.text.toString()
        val encodedPassword = Base64.encodeBytes(passwordText.encodeToByteArray())

        val usersCount = sharedPreferences?.getInt(PARAM_AUTH_USERS_COUNT, 0) ?: 0
        sharedPreferences?.edit()
            ?.putInt(PARAM_AUTH_USERS_COUNT, usersCount + 1)
            ?.putString(PARAM_AUTH_LOGIN, encodedLogin)
            ?.putString(PARAM_AUTH_PASSWORD, encodedPassword)
            ?.putString(PARAM_USER + usersCount, Gson().toJson(user))
            ?.apply()
    }
}