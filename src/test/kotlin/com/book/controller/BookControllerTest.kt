package com.book.controller

import com.book.repository.BookModel
import com.book.service.BookService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model


@WebMvcTest
class BookControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var bookService: BookService

//    @BeforeEach
//    fun initMock() {
//        MockitoAnnotations.openMocks(this)
//    }

    @Test
    fun init() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("index"))
            .andExpect(model().attribute("books", listOf<BookModel>()));
    }

    @Test
    @DisplayName("searchBook() 検索結果1件のケース")
    fun searchBook1() {

        val books = listOf(BookModel(0,"",""))
        `when`(bookService.searchBook("all","aaa"))
            .thenReturn(books)

        mockMvc.perform(post("/search")
            .param("searchType", "all")
            .param("searchKeyword", "aaa"))
            .andExpect(model().attribute("books", books))
            .andExpect(model().attribute("message", "1件を表示"))
            .andExpect(model().attribute("searchKeyword", "aaa"))
            .andExpect(model().attribute("searchType", "all"))
            .andExpect(view().name("index"))
    }
    @Test
    @DisplayName("searchBook() 検索結果0件のケース")
    fun searchBook2() {
        val books = listOf<BookModel>()
        `when`(bookService.searchBook("all","aaa"))
            .thenReturn(books)

        mockMvc.perform(post("/search")
            .param("searchType", "all")
            .param("searchKeyword", "aaa"))
            .andExpect(model().attribute("books", books))
            .andExpect(model().attribute("message", "一致する結果は見つかりませんでした。"))
            .andExpect(model().attribute("searchKeyword", "aaa"))
            .andExpect(model().attribute("searchType", "all"))
            .andExpect(view().name("index"))
    }

    @Test
    fun showAddPage() {
        mockMvc.perform(get("/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
            .andExpect(model().attribute("bookForm", BookForm()));
    }

    @Test
    @DisplayName("addBook() 入力チェックOK")
    fun addBook1() {

        val bookForm = BookForm("title1", "author1")

        mockMvc.perform(post("/add").flashAttr("bookForm", bookForm))
            .andExpect(model().hasNoErrors())
            .andExpect(model().attribute("book", bookForm))
            .andExpect(model().attribute("action", "登録"))
            .andExpect(view().name("complete"))
    }

    @Test
    @DisplayName("addBook() 入力チェックNG：title未設定")
    fun addBook2() {
        val bookForm = BookForm("", "author1")
        mockMvc.perform(post("/add").flashAttr("bookForm", bookForm))
            .andExpect(model().hasErrors())
            .andExpect(view().name("add"))
    }

    @Test
    @DisplayName("addBook() 入力チェックNG：author未設定")
    fun addBook3() {
        val bookForm = BookForm("title1", "")
        mockMvc.perform(post("/add").flashAttr("bookForm", bookForm))
            .andExpect(model().hasErrors())
            .andExpect(view().name("add"))
    }

    @Test
    fun showEditPage1() {
        val book = BookModel(0, "title1", "author1")
        `when`(bookService.selectById(0))
            .thenReturn(book)

        mockMvc.perform(post("/showEdit").param("id", "0"))
            .andExpect(view().name("edit"))
            .andExpect(model().attribute("bookForm", BookForm("title1", "author1")))
            .andExpect(model().attribute("book", book))
    }

    @Test
    @DisplayName("showEditPage() 更新対象のbookが取得できない")
    fun showEditPage2() {
        `when`(bookService.selectById(0))
            .thenReturn(null)
        mockMvc.perform(post("/showEdit").param("id", "0"))
            .andExpect(view().name("index"))
            .andExpect(model().attribute("books", listOf<BookModel>()))
    }

    @Test
    @DisplayName("editBook() 入力チェックOK")
    fun editBook1() {
        val bookForm = BookForm("title1", "author1")
        mockMvc.perform(post("/edit")
            .param("edit", "")
            .param("id", "0")
            .flashAttr("bookForm", bookForm))
            .andExpect(model().hasNoErrors())
            .andExpect(view().name("complete"))
            .andExpect(model().attribute("book", bookForm))
            .andExpect(model().attribute("action", "編集"))
    }
    @Test
    @DisplayName("editBook() 入力チェックNG：title未設定")
    fun editBook2() {
        val bookForm = BookForm("", "author1")
        mockMvc.perform(post("/edit")
            .param("edit", "")
            .param("id", "0")
            .flashAttr("bookForm", bookForm))
            .andExpect(model().hasErrors())
            .andExpect(view().name("edit"))
            .andExpect(model().attribute("book", BookModel(0, bookForm.title, bookForm.author)))
    }
    @Test
    @DisplayName("editBook() 入力チェックNG：author未設定")
    fun editBook3() {
        val bookForm = BookForm("title", "")
        mockMvc.perform(post("/edit")
            .param("edit", "")
            .param("id", "0")
            .flashAttr("bookForm", bookForm))
            .andExpect(model().hasErrors())
            .andExpect(view().name("edit"))
            .andExpect(model().attribute("book", BookModel(0, bookForm.title, bookForm.author)))
    }

    @Test
    fun deleteBook() {
        mockMvc.perform(post("/edit")
            .param("delete", "")
            .param("id", "0"))
            .andExpect(view().name("complete"))
            .andExpect(model().attribute("action", "削除"))
    }


}


