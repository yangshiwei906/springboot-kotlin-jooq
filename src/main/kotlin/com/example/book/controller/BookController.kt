package com.example.book.controller

import com.example.book.dto.Book
import com.example.book.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController( private val bookService: BookService) {

    @GetMapping("")
    // すべてのbookを取得
    fun getAllBooks(): String {
        return bookService.getAllBooks()
    }

    @GetMapping("/{id}")
    // idが一致するbookを取得
    fun getBookById(@PathVariable id: String):String{
        return bookService.getById(id)
    }

    @GetMapping("/search")
    // seaarch?author=xxx でauthorに一致するbookを取得
    fun getBookByAuthor(@RequestParam author: String): String{
        return bookService.getByAuthor(author)
    }

    @PostMapping
    // bookを一件登録
    fun createBook(@RequestBody book: Book) :String{
        return bookService.createBook(book)
    }

    @PutMapping("/{id}")
    // idに一致するbookの更新
    fun updateBook(@PathVariable id: String,@RequestBody book: Book) : String{
        return bookService.updateBook(id ,book)
    }

}