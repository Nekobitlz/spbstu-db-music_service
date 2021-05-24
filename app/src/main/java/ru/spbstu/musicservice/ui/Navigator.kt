package ru.spbstu.musicservice.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.ui.auth.AuthFragment
import ru.spbstu.musicservice.ui.charts.ChartsFragment
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment
import ru.spbstu.musicservice.ui.payments.PaymentsFragment
import ru.spbstu.musicservice.ui.register.RegisterFragment
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

    fun toAuth(addToBackstack: Boolean = false) {
        navigateTo(AuthFragment(), addToBackstack = addToBackstack)
    }

    fun toMusicFeed(user: User, popBackstack: Boolean = false) {
        navigateTo(
            fragment = MusicFeedFragment(),
            args = Bundle().apply {
                putSerializable(MusicFeedFragment.PARAM_USER, user)
            },
            popBackstack = popBackstack
        )
    }

    fun toPayments(user: User) {
        navigateTo(
            fragment = PaymentsFragment(),
            args = Bundle().apply {
                putSerializable(MusicFeedFragment.PARAM_USER, user)
            },
            addToBackstack = true
        )
    }

    fun toCharts() {
        navigateTo(ChartsFragment())
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

    fun toRegister() {
        navigateTo(RegisterFragment(), addToBackstack = true)
    }

    private fun navigateTo(
        fragment: Fragment,
        args: Bundle? = null,
        addToBackstack: Boolean = false,
        popBackstack: Boolean = false,
    ) = activity?.let { activity ->
        fragment.arguments = args
        activity.supportFragmentManager.apply {
            if (popBackstack) popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            beginTransaction()
                .replace(R.id.container, fragment)
                .apply { if (addToBackstack) addToBackStack(null) }
                .commit()
        }
    }
}