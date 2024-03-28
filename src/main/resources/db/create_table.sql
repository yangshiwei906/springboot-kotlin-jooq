CREATE TABLE book
(
    book_id        VARCHAR(36)  NOT NULL PRIMARY KEY,
    book_title      VARCHAR(255) NOT NULL,
    isbn_code      VARCHAR(255),
    author_name  VARCHAR(100) NOT NULL
);