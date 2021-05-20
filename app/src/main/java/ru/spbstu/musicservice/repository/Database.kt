package ru.spbstu.musicservice.repository

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class Database(
    host: String,
    port: Int,
    database: String,
    private val user: String,
    val password: String
) {
    private var connection: Connection? = null
    private var url = "jdbc:postgresql://%s:%d/%s"
    private var status = false
    val logTag = "DATABASE"

    init {
        url = String.format(url, host, port, database)
        connect()
        //this.disconnect();
        Log.d(logTag, "connection status:$status")
    }

    val extraConnection: Connection?
        get() {
            var c: Connection? = null
            try {
                Class.forName("org.postgresql.Driver")
                c = DriverManager.getConnection(url, user, password)
            } catch (e: Exception) {
                Log.d(logTag, e.message ?: "")
            }
            return c
        }

    fun getUsers() = Thread {
        Log.d(
            logTag,
            connection?.createStatement()
                ?.executeQuery("SELECT * FROM db.users")?.statement.toString()
        )
    }.start()

    fun select(query: String): ResultSet? {
        return connection?.createStatement()?.executeQuery(query)
    }

    private fun connect() {
        try {
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(url, user, password)
            status = true
            Log.d(logTag, "connected:$status")
        } catch (e: Exception) {
            status = false
            Log.d(logTag, e.message ?: "")
        }
    }
}