package ru.spbstu.musicservice.ui.auth

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
class AuthViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _authState = MutableLiveData<State<User>>()
    val authState: LiveData<State<User>>
        get() = _authState

    fun onSubmitClick(login: String, password: String) {
        viewModelScope.launch {
            _authState.value = State.Loading()
            withContext(Dispatchers.IO) {
                try {
                    val user = databaseRepository.getUser(login, password)
                    val state = if (user == null) {
                        State.Error(NoSuchElementException())
                    } else {
                        State.Success(user)
                    }
                    _authState.postValue(state)
                } catch (t: Throwable) {
                    _authState.postValue(State.Error(t))
                }
            }
        }
    }
}