package ru.spbstu.musicservice.repository

import ru.spbstu.musicservice.data.Cd
import ru.spbstu.musicservice.data.Chart
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val database: Database
) {

    fun getCds(count: Int = 10): List<Cd> {
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

    fun getCharts(count: Int = 10): List<Chart> {
        val resultSet = database.select(
            "SELECT * FROM db.chart ORDER BY chart_start_date DESC LIMIT $count"
        ) ?: return listOf()
        val result = mutableListOf<Chart>()
        while (resultSet.next()) {
            result.add(
                Chart(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("chart_start_date"),
                    resultSet.getString("chart_end_date"),
                    resultSet.getInt("version")
                )
            )
        }
        return result
    }
}