package ru.spbstu.musicservice.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import ru.spbstu.musicservice.R
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

    fun navigateTo(
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