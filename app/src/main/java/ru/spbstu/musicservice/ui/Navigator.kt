package ru.spbstu.musicservice.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.auth.AuthFragment
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import ru.spbstu.musicservice.ui.user_info.UserInfoFragment
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class Navigator @Inject constructor(
    @ActivityContext activityContext: Context
) {

    private val activityReference = WeakReference<AppCompatActivity>(
        activityContext as? AppCompatActivity
    )
    private val activity: AppCompatActivity?
        get() = activityReference.get()

    fun toAuth() {
        navigateTo(AuthFragment())
    }

    fun toMusicFeed(user: User) {
        navigateTo(
            fragment = MusicFeedFragment(),
            args = Bundle().apply {
                putSerializable(MusicFeedFragment.PARAM_USER, user)
            }
        )
    }

    fun toUserInfo(user: User) {
        navigateTo(
            fragment = UserInfoFragment(),
            args = Bundle().apply {
                putSerializable(MusicFeedFragment.PARAM_USER, user)
            },
            addToBackstack = true
        )
    }

    private fun navigateTo(
        fragment: Fragment,
        args: Bundle? = null,
        addToBackstack: Boolean = false
    ): Int? {
        return activity?.let { activity ->
            fragment.arguments = args
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .apply { if (addToBackstack) addToBackStack(null) }
                .commit()
        }
    }
}