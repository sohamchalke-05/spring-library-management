package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.entities.AuthorEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.repository.AuthorRepo;
import com.LibraryManagementHomework.LibraryManagement.repository.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    Logger logger = LoggerFactory.getLogger(AuthorService.class);

    public AuthorService(AuthorRepo authorRepo, BookRepo bookRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    public AuthorEntity getAuthorById(Long authorId) {
        logger.trace("getAuthorById() called with id: {}", authorId);
        try {
            logger.info("Attempting to fetch author with ID {}", authorId);
            AuthorEntity authorEntity = authorRepo.findById(authorId).orElse(null);
            if (authorEntity != null) {
                logger.debug("Author found: {}", authorEntity.getName());
            } else {
                logger.warn("Author not found for ID {}", authorId);
            }
            return authorEntity;
        } catch (Exception e) {
            logger.error("Exception occurred in getAuthorById", e);
            throw new RuntimeException("Error fetching author");
        }
    }

    public List<AuthorEntity> getAllAuthor() {
        logger.trace("getAllAuthor() called");
        try {
            List<AuthorEntity> authors = authorRepo.findAll();
            logger.info("Retrieved {} authors", authors.size());
            return authors;
        } catch (Exception e) {
            logger.error("Failed to retrieve all authors", e);
            throw new RuntimeException("Error retrieving authors");
        }
    }

    public AuthorEntity createNewAuthor(AuthorEntity authorEntity) {
        logger.trace("createNewAuthor() called with author: {}", authorEntity.getName());
        try {
            AuthorEntity savedAuthor = authorRepo.save(authorEntity);
            logger.info("Author created successfully with ID: {}", savedAuthor.getId());
            return savedAuthor;
        } catch (Exception e) {
            logger.error("Failed to create author", e);
            throw new RuntimeException("Error creating author");
        }
    }

    public boolean deleteAuthorById(Long authorId) {
        logger.trace("deleteAuthorById() called with ID: {}", authorId);
        try {
            if (!authorRepo.existsById(authorId)) {
                logger.warn("Author ID {} does not exist", authorId);
                return false;
            }
            authorRepo.deleteById(authorId);
            logger.info("Author with ID {} deleted successfully", authorId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete author with ID {}", authorId, e);
            throw new RuntimeException("Error deleting author");
        }
    }

    public List<AuthorEntity> findByName(String name) {
        logger.trace("findByName() called with name: {}", name);
        try {
            List<AuthorEntity> authors = authorRepo.findByName(name);
            logger.info("Found {} authors with name '{}'", authors.size(), name);
            return authors;
        } catch (Exception e) {
            logger.error("Failed to find authors by name '{}'", name, e);
            throw new RuntimeException("Error finding authors by name");
        }
    }

    public AuthorEntity updateAuthor(AuthorEntity authorEntity, Long authorId) {
        logger.trace("updateAuthor() called with ID: {}", authorId);
        try {
            AuthorEntity existingAuthor = authorRepo.findById(authorId)
                    .orElseThrow(() -> new NullPointerException("Author not found with ID " + authorId));
            existingAuthor.setName(authorEntity.getName());
            existingAuthor.setBooks(authorEntity.getBooks());

            AuthorEntity updatedAuthor = authorRepo.save(existingAuthor);
            logger.info("Author with ID {} updated successfully", authorId);
            return updatedAuthor;
        } catch (Exception e) {
            logger.error("Failed to update author with ID {}", authorId, e);
            throw new RuntimeException("Error updating author");
        }
    }

    public AuthorEntity assignAuthorToBook(Long authorId, Long bookId) {
        logger.trace("assignAuthorToBook() called with authorId: {}, bookId: {}", authorId, bookId);
        try {
            Optional<AuthorEntity> optionalAuthor = authorRepo.findById(authorId);
            Optional<BookEntity> optionalBook = bookRepo.findById(bookId);

            if (optionalAuthor.isPresent() && optionalBook.isPresent()) {
                AuthorEntity author = optionalAuthor.get();
                BookEntity book = optionalBook.get();

                book.setAuthor(author);
                bookRepo.save(book);

                author.getBooks().add(book);
                logger.info("Book ID {} assigned to Author ID {}", bookId, authorId);
                return author;
            } else {
                logger.warn("Author or Book not found (Author ID: {}, Book ID: {})", authorId, bookId);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error assigning book to author (Author ID: {}, Book ID: {})", authorId, bookId, e);
            throw new RuntimeException("Error assigning book to author");
        }
    }
}


