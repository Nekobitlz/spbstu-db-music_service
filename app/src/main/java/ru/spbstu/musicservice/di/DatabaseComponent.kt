package ru.spbstu.musicservice.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.spbstu.musicservice.repository.Database

@Module
@InstallIn(SingletonComponent::class)
object DatabaseComponent {

    @Provides
    fun provideDatabase(): Database {
        return Database(
            host = "10.0.2.2",
            port = 5432,
            database = "postgres",
            user = "andrey.matveets",
            password = ""
        )
    }
}