package com.book.service

import com.book.repository.BookModel
import com.book.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService {

    @Autowired
    lateinit var bookRepository: BookRepository

    fun searchBook(searchType: String, searchKeyword: String): List<BookModel> {

        val books = if (searchKeyword.isNotBlank() && searchType == "all") {
            bookRepository.selectByTitleOrAuthor(searchKeyword)
        } else if (searchKeyword.isNotBlank() && searchType == "title") {
            bookRepository.selectByTitle(searchKeyword)
        } else if (searchKeyword.isNotBlank() && searchType == "author") {
            bookRepository.selectByAuthor(searchKeyword)
        } else {
            bookRepository.selectAll()
        }
        return books
    }

    fun selectById(id: Int): BookModel? {
        return bookRepository.selectById(id)
    }

    fun insert(title: String, author: String) {
        bookRepository.insert(title, author)
    }

    fun update(id: Int, title: String, author: String) {
        bookRepository.update(id, title, author)
    }

    fun delete(id: Int) {
        bookRepository.delete(id)
    }
}