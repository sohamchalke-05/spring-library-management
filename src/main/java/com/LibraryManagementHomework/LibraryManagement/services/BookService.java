package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.repository.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public BookEntity getBookById(Long bookId) {
        logger.trace("getBookById() called with ID: {}", bookId);
        try {
            BookEntity book = bookRepo.findById(bookId).orElse(null);
            if (book != null) {
                logger.info("Book found with ID: {}", bookId);
            } else {
                logger.warn("Book not found with ID: {}", bookId);
            }
            return book;
        } catch (Exception e) {
            logger.error("Error retrieving book by ID: {}", bookId, e);
            throw new RuntimeException("Error fetching book");
        }
    }

    public List<BookEntity> getAllBooks() {
        logger.trace("getAllBooks() called");
        try {
            List<BookEntity> books = bookRepo.findAll();
            logger.info("Retrieved {} books from repository", books.size());
            return books;
        } catch (Exception e) {
            logger.error("Failed to retrieve all books", e);
            throw new RuntimeException("Error fetching book list");
        }
    }

    public BookEntity createNewBook(BookEntity bookEntity) {
        logger.trace("createNewBook() called with title: {}", bookEntity.getTitle());
        try {
            BookEntity savedBook = bookRepo.save(bookEntity);
            logger.info("Book created with ID: {}", savedBook.getId());
            return savedBook;
        } catch (Exception e) {
            logger.error("Error creating new book", e);
            throw new RuntimeException("Book creation failed");
        }
    }


    public boolean deleteBookById(Long bookId) {
        logger.trace("deleteBookById() called with ID: {}", bookId);
        try {
            if (!bookRepo.existsById(bookId)) {
                logger.warn("Cannot delete. Book with ID {} does not exist", bookId);
                return false;
            }
            bookRepo.deleteById(bookId);
            logger.info("Book with ID {} deleted successfully", bookId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting book with ID: {}", bookId, e);
            throw new RuntimeException("Error deleting book");
        }
    }


    public List<BookEntity> findByTitle(String title) {
        logger.trace("findByTitle() called with title: {}", title);
        try {
            List<BookEntity> books = bookRepo.findByTitle(title);
            logger.info("Found {} books with title '{}'", books.size(), title);
            return books;
        } catch (Exception e) {
            logger.error("Error finding books by title: {}", title, e);
            throw new RuntimeException("Error finding books");
        }
    }

    public List<BookEntity> findByPublishedDate(LocalDate date) {
        logger.trace("findByPublishedDate() called after date: {}", date);
        try {
            List<BookEntity> books = bookRepo.findByPublishedDateAfter(date);
            logger.info("Found {} books published after {}", books.size(), date);
            return books;
        } catch (Exception e) {
            logger.error("Error finding books by published date after {}", date, e);
            throw new RuntimeException("Error finding books");
        }
    }

    public BookEntity updateAuthor(BookEntity bookEntity, Long bookId) {
        logger.trace("updateAuthor() called for book ID: {}", bookId);
        try {
            BookEntity existingBook = bookRepo.findById(bookId)
                    .orElseThrow(() -> new NullPointerException("Book not found with ID " + bookId));
            existingBook.setTitle(bookEntity.getTitle());
            existingBook.setPublishedDate(bookEntity.getPublishedDate());
            existingBook.setPrice(bookEntity.getPrice());
            existingBook.setAuthor(bookEntity.getAuthor());

            BookEntity updatedBook = bookRepo.save(existingBook);
            logger.info("Author updated for book ID: {}", bookId);
            return updatedBook;
        } catch (Exception e) {
            logger.error("Failed to update author for book ID: {}", bookId, e);
            throw new RuntimeException("Error updating book author");
        }
    }

    public List<BookEntity> findByAuthorName(String name) {
        logger.trace("findByAuthorName() called with name: {}", name);
        try {
            List<BookEntity> books = bookRepo.findByAuthorName(name);
            logger.info("Found {} books by author '{}'", books.size(), name);
            return books;
        } catch (Exception e) {
            logger.error("Error finding books by author name: {}", name, e);
            throw new RuntimeException("Error finding books by author");
        }
    }

    public BookEntity updateBook(BookEntity bookEntity, Long bookId) {
        logger.trace("updateBook() called for book ID: {}", bookId);
        try {
            BookEntity existingBook = bookRepo.findById(bookId)
                    .orElseThrow(() -> new NullPointerException("Book not found with ID " + bookId));
            existingBook.setTitle(bookEntity.getTitle());
            existingBook.setPrice(bookEntity.getPrice());
            existingBook.setPublishedDate(bookEntity.getPublishedDate());
            existingBook.setAuthor(bookEntity.getAuthor());

            BookEntity updatedBook = bookRepo.save(existingBook);
            logger.info("Book with ID {} updated successfully", bookId);
            return updatedBook;
        } catch (Exception e) {
            logger.error("Failed to update book with ID: {}", bookId, e);
            throw new RuntimeException("Error updating book");
        }
    }
}
