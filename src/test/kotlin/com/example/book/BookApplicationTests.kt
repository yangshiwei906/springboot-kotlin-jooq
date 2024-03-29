package com.example.book

import com.example.book.dto.Book
import com.example.book.dto.ResultBook
import com.example.book.dto.ResultBooks
import com.example.book.dto.ResultNG
import com.example.book.jooq.public.tables.records.BookRecord
import com.example.book.jooq.public.tables.references.BOOK
import com.example.book.repository.BookRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class BookApplicationTest {

	@Mock
	private lateinit var dslContext: DSLContext

	@InjectMocks
	private lateinit var bookRepository: BookRepository

	private val objectMapper = ObjectMapper()

	@BeforeEach
	fun setUp() {
		MockitoAnnotations.openMocks(this)
	}

	@Test
	fun testFindAll() {
		val mockRecord = mock<Record>()
		val mockResult = mock<Result<Record>>()
		whenever(dslContext.select().from(BOOK).fetch()).thenReturn(mockResult)
		whenever(mockResult.map<Record>(any())).thenReturn(listOf(toRecord(mockRecord)))

		val result = bookRepository.findAll()

		assertEquals(objectMapper.writeValueAsString(ResultBooks(1, listOf(toModel()))), result)
	}


	@Test
	fun testFindAllNoData() {
		val mockResult = mock<Result<Record>>()
		whenever(dslContext.select().from(BOOK).fetch()).thenReturn(mockResult)
		whenever(mockResult.map<Record>(any())).thenReturn(emptyList())

		val result = bookRepository.findAll()

		assertEquals(objectMapper.writeValueAsString(ResultNG(0, "DATA_NOT_FOUND")), result)
	}

	@Test
	fun testFindById() {
		val mockRecord = mockRecord()
		whenever(dslContext.select().from(BOOK).where(BOOK.BOOK_ID.eq(any<String>())).fetchOne()).thenReturn(mockRecord)

		val result = bookRepository.findById("1")

		assertEquals(objectMapper.writeValueAsString(ResultBook(1, toModel())), result)
	}

	@Test
	fun testFindByIdNoData() {
		whenever(dslContext.select().from(BOOK).where(BOOK.BOOK_ID.eq(any<String>())).fetchOne()).thenReturn(null)

		val result = bookRepository.findById("1")

		assertEquals(objectMapper.writeValueAsString(ResultNG(0, "DATA_NOT_FOUND")), result)
	}

	@Test
	fun testFindByAuthor() {
		val mockRecord = mockRecord()
		val mockResult = mock<Result<Record>>()
		whenever(dslContext.select().from(BOOK).where(BOOK.AUTHOR_NAME.eq(any<String>())).fetch()).thenReturn(mockResult)
		whenever(mockResult.map<Record>(any())).thenReturn(listOf(toRecord(mockRecord)))

		val result = bookRepository.findByAuthor("Author")

		assertEquals(objectMapper.writeValueAsString(ResultBooks(1, listOf(toModel()))), result)
	}

	@Test
	fun testFindByAuthorNoData() {
		val mockResult = mock<Result<Record>>()
		whenever(dslContext.select().from(BOOK).where(BOOK.AUTHOR_NAME.eq(any<String>())).fetch()).thenReturn(mockResult)
		whenever(mockResult.map<Record>(any())).thenReturn(emptyList())

		val result = bookRepository.findByAuthor("Author")

		assertEquals(objectMapper.writeValueAsString(ResultNG(0, "DATA_NOT_FOUND")), result)
	}

	@Test
	fun testCreateBook() {
		val book = Book("1", "Title", "ISBN123", "Author")
		val mockBook = mock<Book>()
		val mockRecord = mock<Record4<String?, String?, String?, String?>>()
		whenever(dslContext.newRecord(
				BOOK.BOOK_ID,
				BOOK.BOOK_TITLE,
				BOOK.AUTHOR_NAME,
				BOOK.ISBN_CODE)
		).thenReturn(mockRecord)

		whenever(mockRecord.value1()).thenReturn("1")
		whenever(mockRecord.value2()).thenReturn(mockBook.bookTitle)
		whenever(mockRecord.value3()).thenReturn(mockBook.authorName)
		whenever(mockRecord.value4()).thenReturn(mockBook.isbnCode)

		val result = bookRepository.createBook(book)

		assertEquals(objectMapper.writeValueAsString(ResultBook(1, book)), result)
	}

	@Test
	fun testUpdateBook() {
		val mockResult = mock<UpdateSetFirstStep<BookRecord>>()
		whenever(dslContext.update(BOOK)).thenReturn(mockResult)

		val result = bookRepository.updateBook("1", Book("1", "Title", "ISBN123", "Author"))

		assertEquals(objectMapper.writeValueAsString(ResultBook(1, toModel())), result)
	}

	private fun toModel(): Book {

		return Book("1", "Title", "ISBN123", "Author")
	}

	private fun toRecord(record: Record): Record {

		return record
	}

	private fun mockRecord(): Record = mock()

//	private fun mockResult() = 1
}
