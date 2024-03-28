package com.example.book.dto

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Book(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val bookId: String = "",
        val bookTitle: String = "",
        val isbnCode: String? = null,
        val authorName: String = "",
)