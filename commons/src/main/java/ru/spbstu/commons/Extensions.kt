package ru.spbstu.commons

fun <T> lazyUnsychronized(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)