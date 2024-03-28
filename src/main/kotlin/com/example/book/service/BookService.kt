package com.example.book.service

import com.example.book.dto.Book
import com.example.book.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(private val bookRepository: BookRepository){
    @Transactional(readOnly = true)
    fun getAllBooks(): String {
        return bookRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getById(id: String): String {
        return bookRepository.findById(id)
    }

    @Transactional(readOnly = true)
    fun getByAuthor(authorName: String): String {
        return bookRepository.findByAuthor(authorName)
    }

    @Transactional
    fun createBook(book:Book): String {
        return bookRepository.createBook(book)
    }

    @Transactional
    fun updateBook(id: String, book:Book): String {
        return bookRepository.updateBook(id, book)
    }
}