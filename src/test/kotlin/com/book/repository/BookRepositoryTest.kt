package com.book.repository

import org.junit.jupiter.api.Assertions.*
import com.jooq.db.tables.references.BOOK
import org.jooq.DSLContext
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositoryTest {

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var ctx: DSLContext

    @BeforeEach
    // 各テストケース実行前に全件削除
    fun beforeEach(){
        ctx.delete(BOOK).execute()
    }

    @Test
    fun selectById() {
        // 1件登録
        val result= ctx.insertInto(BOOK)
            .set(BOOK.TITLE, "title1")
            .set(BOOK.AUTHOR, "author1").returningResult(BOOK.ID)
            .fetchOne()
        val id = result?.getValue(BOOK.ID)

        val book = bookRepository.selectById(id!!)
        assertNotNull(book)
        assertEquals(id, book?.id)
        assertEquals("title1", book?.title, )
        assertEquals("author1", book?.author)
    }

    @Test
    @DisplayName("取得できないケース")
    fun selectById2() {
        // 1件登録
        val result= ctx.insertInto(BOOK)
            .set(BOOK.TITLE, "title1")
            .set(BOOK.AUTHOR, "author1").returningResult(BOOK.ID)
            .fetchOne()
        val id = result?.getValue(BOOK.ID)

        val book = bookRepository.selectById(id!!+1) // 登録したidに+1して検索
        assertNull(book)
    }

    @Test
    fun selectAll() {
        // 2件登録
        val result1= ctx.insertInto(BOOK).set(BOOK.TITLE, "title1")
            .set(BOOK.AUTHOR, "author1").returningResult(BOOK.ID)
            .fetchOne()
        val id1 = result1?.getValue(BOOK.ID)

        val result2= ctx.insertInto(BOOK)
            .set(BOOK.TITLE, "title2")
            .set(BOOK.AUTHOR, "author2").returningResult(BOOK.ID)
            .fetchOne()
        val id2 = result2?.getValue(BOOK.ID)

        val books = bookRepository.selectAll()
        assertEquals(books.size, 2)
        assertEquals(books[0].id, id1)
        assertEquals("title1",books[0].title)
        assertEquals("author1", books[0].author)

        assertEquals(books[1].id, id2)
        assertEquals("title2",books[1].title)
        assertEquals("author2", books[1].author)
    }

    @Test
    fun selectByTitle() {
        // 3件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "タイトル").set(BOOK.AUTHOR, "田中").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "タイトル").set(BOOK.AUTHOR, "田中").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title3").set(BOOK.AUTHOR, "author3").execute()

        val books = bookRepository.selectByTitle("タイトル")
        assertEquals(2, books.size)
    }

    @Test
    fun selectByAuthor() {
        // 3件登録
        ctx.insertInto(BOOK).set(BOOK.TITLE, "タイトル").set(BOOK.AUTHOR, "田中").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "タイトル").set(BOOK.AUTHOR, "田中").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title3").set(BOOK.AUTHOR, "author3").execute()

        val books = bookRepository.selectByAuthor("田中")
        assertEquals(books.size, 2)
    }

    @Test
    fun selectByTitleOrAuthor() {
        ctx.insertInto(BOOK).set(BOOK.TITLE, "田中").set(BOOK.AUTHOR, "author1").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title2").set(BOOK.AUTHOR, "author2").execute()
        ctx.insertInto(BOOK).set(BOOK.TITLE, "title3").set(BOOK.AUTHOR, "田中").execute()

        val books = bookRepository.selectByTitleOrAuthor("田中")
        assertEquals(books.size, 2)
    }

    @Test
    fun insert() {
        bookRepository.insert("タイトル", "田中")

        var records = ctx.select().from(BOOK)
        assertEquals(1, records.count())
    }

    @Test
    fun update() {
        // 1件登録
        val result= ctx.insertInto(BOOK)
            .set(BOOK.TITLE, "title1")
            .set(BOOK.AUTHOR, "author1").returningResult(BOOK.ID)
            .fetchOne()
        val id = result?.getValue(BOOK.ID)!!

        // 更新
        bookRepository.update(id, "title1update","author1update")

        // 更新後のデータ取得を取得して検証
        val book = bookRepository.selectById(id)!!
        assertEquals("title1update", book.title)
        assertEquals("author1update", book.author)
    }


}

