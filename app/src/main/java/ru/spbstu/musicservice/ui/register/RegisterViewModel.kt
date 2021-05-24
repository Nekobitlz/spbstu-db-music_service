package ru.spbstu.musicservice.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.repository.DatabaseRepository
import ru.spbstu.musicservice.ui.State
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _userState = MutableLiveData<State<User>>()
    val userState: LiveData<State<User>>
        get() = _userState

    fun onRegisterClick(user: User, password: String) {
        viewModelScope.launch {
            _userState.value = State.Loading()
            withContext(Dispatchers.IO) {
                try {
                    databaseRepository.insertUser(user, password)
                    val state = State.Success(user)
                    _userState.postValue(state)
                } catch (t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    _userState.postValue(State.Error(t))
                }
            }
        }
    }
}