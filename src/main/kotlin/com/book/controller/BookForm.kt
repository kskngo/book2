package com.book.controller

import javax.validation.constraints.NotBlank

data class BookForm(

    @field:NotBlank(message = "書籍名は必須入力です。")
    val title: String = "",

    @field:NotBlank(message = "著者は必須入力です。")
    val author: String = ""
)