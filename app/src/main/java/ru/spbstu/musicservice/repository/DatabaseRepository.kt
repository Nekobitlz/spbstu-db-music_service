package ru.spbstu.musicservice.repository

import ru.spbstu.musicservice.data.Cd
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    val database: Database
) {

    fun getCd(count: Int = 10): List<Cd> {
        val resultSet = database.select(
            "SELECT * FROM db.cd ORDER BY rating DESC, release_date DESC LIMIT $count"
        ) ?: return listOf()
        val result = mutableListOf<Cd>()
        while (resultSet.next()) {
            result.add(
                Cd(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("release_date"),
                    resultSet.getFloat("rating"),
                    resultSet.getFloat("length")
                )
            )
        }
        return result
    }
}