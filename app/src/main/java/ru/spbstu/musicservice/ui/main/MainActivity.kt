package ru.spbstu.musicservice.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.postgresql.util.Base64
import ru.spbstu.commons.gone
import ru.spbstu.commons.visible
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.databinding.MainActivityBinding
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.State
import javax.inject.Inject

const val APP_STORAGE = "APP_STORAGE"
const val PARAM_AUTH_LOGIN = "PARAM_AUTH_LOGIN"
const val PARAM_AUTH_PASSWORD = "PARAM_AUTH_PASSWORD"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    private val binding: MainActivityBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        binding.progressBar.gone()
        binding.container.visible()
        if (savedInstanceState == null) {
            val sharedPreferences = getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
            val encodedLogin = sharedPreferences.getString(PARAM_AUTH_LOGIN, null)
            val encodedPassword = sharedPreferences.getString(PARAM_AUTH_PASSWORD, null)
            if (encodedLogin == null || encodedPassword == null) {
                openLoginFragment()
            } else {
                val login = String(Base64.decode(encodedLogin))
                val password = String(Base64.decode(encodedPassword))
                viewModel.userState.observe(this) {
                    when (it) {
                        is State.Loading -> {
                            binding.progressBar.visible()
                            binding.container.gone()
                        }
                        is State.Success -> {
                            binding.container.visible()
                            binding.progressBar.gone()
                            navigator.toMusicFeed(it.item)
                        }
                        is State.Error -> {
                            openLoginFragment()
                        }
                    }
                }
                viewModel.onUserRestored(login, password)
            }
        }
    }

    private fun openLoginFragment() {
        binding.progressBar.gone()
        binding.container.visible()
        navigator.toAuth()
    }
}