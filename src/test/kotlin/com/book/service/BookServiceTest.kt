package com.book.service

import com.jooq.db.tables.references.BOOK
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookServiceTest {
    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var ctx: DSLContext

    @BeforeEach
    // 各テストケース実行前に全件削除
    fun beforeEach() {
        ctx.delete(BOOK).execute()
    }

    @Test
    @DisplayName("検索タイプがallの場合、検索キーワード:aaaですべての項目を検索して3件取得")
    fun searchBook() {
        // 5件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author1").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author2").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "aaa").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "aaa").set(BOOK.AUTHOR, "author4").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title4").set(BOOK.AUTHOR, "aaa").execute()

        val books = bookService.searchBook("all", "aaa")
        assertEquals(3, books.size)
    }

    @Test
    @DisplayName("検索タイプがtitleの場合、検索キーワード:aaaでタイトルを検索して1件取得")
    fun searchBook2() {
        // 5件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author1").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author2").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "aaa").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "aaa").set(BOOK.AUTHOR, "author4").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title4").set(BOOK.AUTHOR, "aaa").execute()

        val books = bookService.searchBook("title", "aaa")
        assertEquals(1, books.size)
    }

    @Test
    @DisplayName("検索タイプがauthorの場合、検索キーワード:aaaで著者を検索して2件取得")
    fun searchBook3() {
        // 5件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author1").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author2").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "aaa").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "aaa").set(BOOK.AUTHOR, "author4").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title4").set(BOOK.AUTHOR, "aaa").execute()

        val books = bookService.searchBook("author", "aaa")
        assertEquals(2, books.size)
    }

    @Test
    @DisplayName("検索キーワードがブランクの場合、検索タイプの内容に関係なく全件取得")
    fun searchBook4() {
        // 5件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author1").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "author2").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title1").set(BOOK.AUTHOR, "aaa").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "aaa").set(BOOK.AUTHOR, "author4").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title4").set(BOOK.AUTHOR, "aaa").execute()

        val books = bookService.searchBook("author", "")
        assertEquals(5, books.size)
    }


}