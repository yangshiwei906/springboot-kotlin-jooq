package com.example.book.repository

import com.example.book.dto.Book
import com.example.book.dto.ResultBook
import com.example.book.dto.ResultBooks
import com.example.book.dto.ResultNG
import com.example.book.jooq.public.tables.references.BOOK
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.guepardoapps.kulid.ULID
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val dslContext: DSLContext){
    fun findAll(): String {
        // すべてのbookを取得
        val records = this.dslContext.select()
                .from(BOOK)
                .fetch().map { toModel(it) }
        val objectMapper = jacksonObjectMapper()

        // recordsが空でなければ取得したbookのリストを返す
        if (records.size > 0) {
            val result = ResultBooks(1, records)
            return objectMapper.writeValueAsString(result)
        } else {
            //登録されたbookがない場合はstatus:0を返す
            val result = ResultNG(0, "DATA_NOT_FOUND")
            return objectMapper.writeValueAsString(result)
        }
    }

    fun findById(id: String): String {
        //idと一致するbookを取得
        val record = dslContext.select()
                .from(BOOK)
                .where(BOOK.BOOK_ID.eq(id))
                .fetchOne()?.let { toModel(it) }

        val objectMapper = jacksonObjectMapper()

        // recordがNULLでなければidと一致したbookを返す
        if (record != null) {
            val result = ResultBook(1, record)
            return objectMapper.writeValueAsString(result)
        } else {
            // 与えられたidがテーブルになければstatus:0を返す
            val result = ResultNG(0, "DATA_NOT_FOUND")
            return objectMapper.writeValueAsString(result)
        }
    }

    fun findByAuthor(authorName: String): String {
        // 与えられたauthorNameを条件に一致するbookを取得
        val records = dslContext.select()
                .from(BOOK)
                .where(BOOK.AUTHOR_NAME.eq(authorName))
                .fetch().map { toModel(it) }

        val objectMapper = jacksonObjectMapper()

        // authorに紐づくbookを取得した場合、取得したbookを返す
        if (records.size > 0) {
            val result = ResultBooks(1, records)
            return objectMapper.writeValueAsString(result)
        } else {
            // 与えられたauthorNameがテーブルに存在しなければstatus:0を返す
            val result = ResultNG(0, "DATA_NOT_FOUND")
            return objectMapper.writeValueAsString(result)
        }
    }

    fun createBook(book:Book): String {
        val newBookId = ULID.random()
        val record = dslContext.newRecord(BOOK).also {
            it.bookId = newBookId
            it.bookTitle = book.bookTitle
            it.authorName = book.authorName
            it.isbnCode = book.isbnCode
            it.store()
        }

        val objectMapper = jacksonObjectMapper()
        if (record.bookId != null) {
            val result = ResultBook(1, Book(record.bookId!!, record.bookTitle!!, record.isbnCode!!, record.authorName!!))
            return objectMapper.writeValueAsString(result)
        } else {
            val result = ResultNG(0, "登録が失敗しました!")
            return objectMapper.writeValueAsString(result)
        }
    }

    fun updateBook(id: String, book:Book): String {
        // 与えられたidでbookの内容を更新
        val result = dslContext.update(BOOK)
                .set(BOOK.BOOK_TITLE, book.bookTitle)
                .set(BOOK.ISBN_CODE, book.isbnCode)
                .set(BOOK.AUTHOR_NAME, book.authorName)
                .where(BOOK.BOOK_ID.eq(id))
                .execute()

        // 更新が成功した場合はidと一致するbookを返す
        if (result > 0) {
           return findById(id)
        } else {
            //　更新が失敗したら、status:0を返す
           val objectMapper = jacksonObjectMapper()
           val updateResult = ResultNG(0, "更新が失敗しました!")
           return objectMapper.writeValueAsString(updateResult)
        }
    }

    private fun toModel(record: Record) = Book(
            record.getValue(BOOK.BOOK_ID)!!,
            record.getValue(BOOK.BOOK_TITLE)!!,
            record.getValue(BOOK.ISBN_CODE)!!,
            record.getValue(BOOK.AUTHOR_NAME)!!,
    )

}