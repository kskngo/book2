package com.book.repository

import com.jooq.db.tables.Book.Companion.BOOK
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class BookRepository(val ctx: DSLContext) {

    fun selectById(id: Int): BookModel? {
        val result = ctx.select().from(BOOK).where(BOOK.ID.eq(id)).fetchOne()
        return if (result != null) {
            BookModel(
                result.getValue(BOOK.ID),
                result.getValue(BOOK.TITLE),
                result.getValue(BOOK.AUTHOR)
            )
        } else {
            null
        }
    }

    fun selectAll(): List<BookModel> {
        val result = ctx.select().from(BOOK).fetch()
        return result.map { r: Record? ->
            BookModel(
                r?.getValue(BOOK.ID),
                r?.getValue(BOOK.TITLE),
                r?.getValue(BOOK.AUTHOR)
            )
        }
    }

    fun selectByTitle(title: String): List<BookModel> {
        val result = ctx.select().from(BOOK).where(BOOK.TITLE.eq(title))
        return result.map { r: Record? ->
            BookModel(
                r?.getValue(BOOK.ID),
                r?.getValue(BOOK.TITLE),
                r?.getValue(BOOK.AUTHOR)
            )
        }
    }

    fun selectByAuthor(author: String): List<BookModel> {
        val result = ctx.select().from(BOOK).where(BOOK.AUTHOR.eq(author))
        return result.map { r: Record? ->
            BookModel(
                r?.getValue(BOOK.ID),
                r?.getValue(BOOK.TITLE),
                r?.getValue(BOOK.AUTHOR)
            )
        }
    }

    fun selectByTitleOrAuthor(keyword: String): List<BookModel> {
        val result = ctx.select().from(BOOK).where(BOOK.AUTHOR.eq(keyword).or(BOOK.TITLE.eq(keyword)))
        return result.map { r: Record? ->
            BookModel(
                r?.getValue(BOOK.ID),
                r?.getValue(BOOK.TITLE),
                r?.getValue(BOOK.AUTHOR)
            )
        }
    }

    fun insert(title: String, author: String) {
        ctx.insertInto(BOOK)
            .set(BOOK.TITLE, title)
            .set(BOOK.AUTHOR, author)
            .execute()
        
    }

    fun update(id: Int, title: String, author: String) {
        val a = ctx.update(BOOK)
            .set(BOOK.TITLE, title)
            .set(BOOK.AUTHOR, author)
            .where(BOOK.ID.eq(id))
            .execute()
    }

    fun delete(id: Int) {
        ctx.delete(BOOK).where(BOOK.ID.eq(id)).execute()
    }

}