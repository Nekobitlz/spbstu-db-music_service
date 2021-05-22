package ru.spbstu.musicservice.ui

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val item: T) : State<T>()
    data class Error<T>(val exception: Throwable) : State<T>()
}
