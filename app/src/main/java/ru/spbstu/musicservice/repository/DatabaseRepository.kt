package ru.spbstu.musicservice.repository

import ru.spbstu.musicservice.data.*
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val database: Database
) {

    fun getPlaylists(user: User, count: Int = 10): List<Playlist> {
        val query = "SELECT playlist.* FROM db.users" +
                " INNER JOIN db.user_playlist ON users.id = user_playlist.user_id" +
                " INNER JOIN db.playlist ON user_playlist.playlist_id = playlist.id" +
                " WHERE users.id = ${user.id}" +
                " LIMIT $count"
        val resultSet = database.select(query) ?: return listOf()
        val result = mutableListOf<Playlist>()
        while (resultSet.next()) {
            result.add(
                Playlist(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("update_date"),
                    resultSet.getInt("playbacks_count"),
                )
            )
        }
        return result
    }

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

    fun getUser(login: String, password: String): User? {
        var query = "SELECT * " +
                "FROM db.users " +
                "INNER JOIN db.gender ON users.gender_id = gender.id " +
                "INNER JOIN db.subscription ON users.subscription_id = subscription.id " +
                "INNER JOIN db.country ON users.country_id = country.id " +
                "INNER JOIN db.user_type ON users.user_type_id = user_type.id " +
                "WHERE password = '$password' AND "
        query += (if (login.matches(Regex("[0-9]+"))) "phone_number = '$login';" else "email = '$login';")
        val resultSet = database.select(query) ?: return null
        while (resultSet.next()) {
            return User(
                id = resultSet.getString("id"),
                firstName = resultSet.getString("first_name"),
                secondName = resultSet.getString("second_name"),
                phoneNumber = resultSet.getString("phone_number"),
                birthday = resultSet.getString("birthday"),
                age = resultSet.getInt("age"),
                email = resultSet.getString("email"),
                gender = Gender(
                    resultSet.getString("gender_id"),
                    resultSet.getString("gender")
                ),
                userType = UserType(
                    resultSet.getString("user_type_id"),
                    resultSet.getString("type"),
                ),
                subscription = Subscription(
                    resultSet.getString("subscription_id"),
                    resultSet.getInt("price"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                ),
                country = Country(
                    resultSet.getString("country_id"),
                    resultSet.getString("country_name"),
                ),
            )
        }
        return null
    }
}