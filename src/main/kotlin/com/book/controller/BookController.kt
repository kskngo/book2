package com.book.controller

import com.book.repository.BookModel
import com.book.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class BookController {

    @Autowired
    lateinit var bookService: BookService

    @GetMapping("/")
    fun init(model: Model): String {
        model.addAttribute("books", listOf<BookModel>())
        return "index"
    }

    // 検索ボタン
    @PostMapping("/search")
    fun searchBook(model: Model,
                   @RequestParam searchType: String,
                   @RequestParam searchKeyword: String): String {

        var books = bookService.searchBook(searchType, searchKeyword)

        if (books.isEmpty()) {
            model.addAttribute("message", "一致する結果は見つかりませんでした。")
        } else {
            model.addAttribute("message", "${books.count()}件を表示")
        }

        model.addAttribute("books", books)
        model.addAttribute("searchKeyword", searchKeyword)
        model.addAttribute("searchType", searchType)

        return "index"
    }

    @GetMapping("/add")
    fun showAddPage(model: Model): String {
        model.addAttribute("bookForm", BookForm())
        return "add"
    }

    @PostMapping("/add")
    fun addBook(model: Model,
                @Validated @ModelAttribute bookForm: BookForm,
                bindingResult: BindingResult): String {

        if (bindingResult.hasErrors()) {
            return  "add"
        }

        bookService.insert(bookForm.title, bookForm.author)
        model.addAttribute("book", bookForm)
        model.addAttribute("action", "登録")
        return  "complete"
    }

    @PostMapping("/showEdit")
    fun showEditPage(model: Model, @RequestParam id: Int): String {

        val book = bookService.selectById(id)
        return if (book != null) {
            model.addAttribute("bookForm", BookForm(book.title!!, book.author!!))
            model.addAttribute("book", book)
            "edit"
        } else {
            model.addAttribute("books", listOf<BookModel>())
            "index"
        }
    }

    @PostMapping("/edit", params = ["edit"])
    fun editBook(model: Model,
                 @RequestParam id: Int,
                 @Validated @ModelAttribute bookForm: BookForm,
                 bindingResult: BindingResult): String {

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", BookModel(id,bookForm.title, bookForm.author))
            return "edit"
        }
        bookService.update(id, bookForm.title, bookForm.author)
        model.addAttribute("book", bookForm)
        model.addAttribute("action", "編集")
        return  "complete"
    }

    @PostMapping("/edit", params = ["delete"])
    fun deleteBook(model: Model, @RequestParam id: Int): String {
        bookService.delete(id)
        model.addAttribute("action", "削除")
        return  "complete"
    }

}