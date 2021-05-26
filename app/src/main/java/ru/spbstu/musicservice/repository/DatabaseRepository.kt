package ru.spbstu.musicservice.repository

import ru.spbstu.musicservice.data.*
import java.sql.ResultSet
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val database: Database,
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
                getPlaylistParser(resultSet)
            )
        }
        return result
    }

    fun getPlaylist(playlistId: String): Playlist? {
        val query = "SELECT * FROM db.playlist " +
                "WHERE id = $playlistId " +
                "LIMIT 1;"
        val resultSet = database.select(query) ?: return null
        while (resultSet.next()) {
            return getPlaylistParser(resultSet)
        }
        return null
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
            "SELECT * FROM db.chart ORDER BY id LIMIT $count"
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

    fun getPayments(user: User, count: Int = 15): List<Payment> {
        val resultSet = database.select(
            "SELECT * FROM db.payments " +
                    "WHERE payments_date " +
                    "BETWEEN '${user.subscription.startDate}' AND '${user.subscription.endDate}' " +
                    "LIMIT $count"
        ) ?: return listOf()
        val result = mutableListOf<Payment>()
        while (resultSet.next()) {
            result.add(
                Payment(
                    resultSet.getString("id"),
                    resultSet.getString("payments_date"),
                    resultSet.getInt("amount")
                )
            )
        }
        return result
    }

    fun insertUser(user: User, password: String) {
        val query =
            "INSERT INTO db.users (first_name, second_name, password, email, birthday, phone_number, gender_id, user_type_id, subscription_id, country_id) " +
                    "VALUES ('${user.firstName}', '${user.secondName}', '${password}', '${user.email}', '${user.birthday}', '${user.phoneNumber}', '${user.gender.id}', '${user.userType.id}', '${user.subscription.id}', '${user.country.id}')"
        database.insert(query)
    }

    fun getUser(login: String, password: String): User? {
        var query = "SELECT * " +
                "FROM db.users " +
                "LEFT JOIN db.gender ON users.gender_id = gender.id " +
                "LEFT JOIN db.subscription ON users.subscription_id = subscription.id " +
                "LEFT JOIN db.country ON users.country_id = country.id " +
                "LEFT JOIN db.user_type ON users.user_type_id = user_type.id " +
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

    fun getPlaylistSongs(playlist: Playlist, count: Int): List<Song> {
        val query = """SELECT ${getSongFields()}
         FROM db.playlist
         INNER JOIN db.playlist_song ON playlist.id = playlist_song.playlist_id
         LEFT JOIN db.song ON playlist_song.song_id = song.id
         LEFT JOIN db.genre ON genre.id = song.genre_id
         LEFT JOIN db.song_artist ON song_artist.song_id = song.id
         LEFT JOIN db.artist as ar1 ON song_artist.artist_id = ar1.id
         LEFT JOIN db.role ON ar1.role_id = role.id
         WHERE db.playlist.id = '${playlist.id}' 
         GROUP by song.id
         LIMIT $count;
         """
        val resultSet = database.select(query) ?: return listOf()
        val list = mutableListOf<Song>()
        while (resultSet.next()) {
            val song = getSongParser(resultSet)
            list.add(song)
        }
        return list
    }

    private fun getSongParser(resultSet: ResultSet) = Song(
        id = resultSet.getString("id"),
        name = resultSet.getString("song_name"),
        length = resultSet.getFloat("length"),
        releaseDate = resultSet.getString("release_date"),
        rating = resultSet.getFloat("rating"),
        albumPosition = resultSet.getInt("album_position"),
        playbacksCount = resultSet.getInt("playbacks_count"),
        genre = if (resultSet.getString("genre_id") != null) Genre(
            resultSet.getString("genre_id"),
            resultSet.getString("genre_name")
        ) else null,
        artist = getArtistParser(resultSet)
        /*album = Album(
                    id = resultSet.getString("album.id"),
                    name = resultSet.getString("album.name"),
                    length = resultSet.getFloat("album.length"),
                    releaseDate = resultSet.getString("album.release_date"),
                    rating = resultSet.getFloat("album.rating"),
                    playbacksCount  = resultSet.getInt("album.playbacks_count"),
                ),*/
    )

    private fun getArtistParser(resultSet: ResultSet): Artist? {
        return if (resultSet.getString("artist_id") != null) Artist(
            id = resultSet.getString("artist_id"),
            name = resultSet.getString("artist_name"),
            description = resultSet.getString("description"),
            rating = resultSet.getFloat("artist_rating"),
            role = if (resultSet.getString("role_id") != null) Role(
                resultSet.getString("role_id"),
                resultSet.getString("role_name")
            ) else null,
        ) else null
    }

    private fun getPlaylistParser(resultSet: ResultSet) = Playlist(
        resultSet.getString("id"),
        resultSet.getString("name"),
        resultSet.getString("update_date"),
        resultSet.getInt("playbacks_count"),
    )

    private fun getSongFields() = """
           song.id,
           song.name  as song_name,
           song.length,
           song.release_date,
           song.rating,
           song.album_position,
           song.playbacks_count,
           song.genre_id,
           max(genre.name) as genre_name,
           max(ar1.id)     as artist_id,
           max(ar1.name)   as artist_name,
           max(ar1.description) as description,
           max(ar1.rating) as artist_rating,
           max(role.id)    as role_id,
           max(role.name)  as role_name
       """

    fun getCdSong(cd: Cd, count: Int = 10): List<Song> {
        val query = """SELECT ${getSongFields()}
         FROM db.cd
         INNER JOIN db.song ON song.album_id = cd.id
         LEFT JOIN db.genre ON genre.id = song.genre_id
         LEFT JOIN db.song_artist ON song_artist.song_id = song.id
         LEFT JOIN db.artist as ar1 ON song_artist.artist_id = ar1.id
         LEFT JOIN db.role ON ar1.role_id = role.id
         WHERE cd.id = '${cd.id}' 
         GROUP by song.id
         LIMIT $count;
         """
        val resultSet = database.select(query) ?: return listOf()
        val list = mutableListOf<Song>()
        while (resultSet.next()) {
            val song = getSongParser(resultSet)
            list.add(song)
        }
        return list
    }

    fun getChartSongs(chart: Chart, count: Int = 10): List<Song> {
        val query = """SELECT ${getSongFields()}
         FROM db.chart
         INNER JOIN db.chart_entry ON chart.id = chart_entry.chart_id
         INNER JOIN db.song ON chart_entry.song_id = song.id
         LEFT JOIN db.genre ON genre.id = song.genre_id
         LEFT JOIN db.song_artist ON song_artist.song_id = song.id
         LEFT JOIN db.artist as ar1 ON song_artist.artist_id = ar1.id
         LEFT JOIN db.role ON ar1.role_id = role.id
         WHERE chart.id = '${chart.id}' 
         GROUP by song.id
         LIMIT $count;
         """
        val resultSet = database.select(query) ?: return listOf()
        val list = mutableListOf<Song>()
        while (resultSet.next()) {
            val song = getSongParser(resultSet)
            list.add(song)
        }
        return list
    }

    fun addSong(playlist: Playlist, song: Song): Boolean {
        val query = "INSERT INTO db.playlist_song (playlist_id, song_id) VALUES ('${playlist.id}', '${song.id}');"
        database.insert(query)
        return true
    }

    fun removeSong(playlist: Playlist, song: Song): Boolean {
        val query = "DELETE from db.playlist_song where playlist_id='${playlist.id}' AND song_id='${song.id}';"
        database.insert(query)
        return true
    }

    fun searchSongs(query: String, count: Int): List<Song> {
        val request = """SELECT ${getSongFields()}
         FROM db.song
         LEFT JOIN db.genre ON genre.id = song.genre_id
         LEFT JOIN db.song_artist ON song_artist.song_id = song.id
         LEFT JOIN db.artist as ar1 ON song_artist.artist_id = ar1.id
         LEFT JOIN db.role ON ar1.role_id = role.id
         WHERE song.name LIKE '%$query%'
         GROUP by song.id
         LIMIT $count;
         """
        val resultSet = database.select(request) ?: return listOf()
        val list = mutableListOf<Song>()
        while (resultSet.next()) {
            val song = getSongParser(resultSet)
            list.add(song)
        }
        return list
    }
}