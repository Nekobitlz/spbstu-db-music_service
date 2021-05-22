package ru.spbstu.musicservice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.musicservice.ui.Navigator
import ru.spbstu.musicservice.ui.auth.AuthFragment
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import javax.inject.Inject

const val APP_STORAGE = "APP_STORAGE"
const val PARAM_LOGIN_TOKEN = "PARAM_LOGIN_TOKEN"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val sharedPreferences = getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(PARAM_LOGIN_TOKEN, null)
        if (savedInstanceState == null) {
            navigator.navigateTo(if (token != null) MusicFeedFragment() else AuthFragment())
        }
    }
}