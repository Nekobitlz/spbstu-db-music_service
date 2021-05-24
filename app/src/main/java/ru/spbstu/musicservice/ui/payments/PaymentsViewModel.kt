package ru.spbstu.musicservice.ui.payments

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
class PaymentsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    private val _items = MutableLiveData<State<List<PaymentItem>>>()
    val items: LiveData<State<List<PaymentItem>>>
        get() = _items

    fun loadPayments(user: User) {
        viewModelScope.launch {
            _items.postValue(State.Loading())
            withContext(Dispatchers.IO) {
                try {
                    val list = mutableListOf<PaymentItem>()
                    databaseRepository.getPayments(user)
                        .map {
                            PaymentItem(it)
                        }.also {
                            list.addAll(it)
                        }
                    if (list.isEmpty()) {
                        _items.postValue(State.Error(NoSuchElementException()))
                    } else {
                        _items.postValue(State.Success(list.toList()))
                    }
                } catch (t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    _items.postValue(State.Error(t))
                }
            }
        }
    }

    fun onRefresh(user: User) {
        loadPayments(user)
    }
}