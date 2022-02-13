package com.book

import com.jooq.db.tables.references.BOOK
import org.jooq.Record
import org.jooq.Result
import org.jooq.SQLDialect

import org.jooq.impl.DSL
import java.sql.DriverManager
import java.sql.SQLException

class ConnectDatabase

fun main(args: Array<String>) {

    val userName = "sa"
    val password = ""
    val url = "jdbc:h2:~/library"

    try {
        DriverManager.getConnection(url, userName, password).use { conn ->

            val create = DSL.using(conn, SQLDialect.MYSQL)
            val result: Result<Record> = create.select().from(BOOK).fetch()

            for (r in result) {
                val id: Int? = r.getValue(BOOK.ID)
                val title: String? = r.getValue(BOOK.TITLE)
                val author: String? = r.getValue(BOOK.AUTHOR)
                println("ID: $id title: $title author: $author")
            }

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}