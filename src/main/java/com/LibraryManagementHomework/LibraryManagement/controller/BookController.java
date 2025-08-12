package com.LibraryManagementHomework.LibraryManagement.controller;

import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping(path = "/{book_id}")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
//    @PreAuthorize("@SessionMapping.hasPlan(T(com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub).BASIC)")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Long book_id){
        BookEntity book =  bookService.getBookById(book_id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookEntity>> getAllBooks(){
        List<BookEntity> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }


    @PostMapping
    // We can also Customize methods in @PreAuthorize
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("@SessionMapping.hasPlan(T(com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub).PREMIUM)")
    public ResponseEntity<BookEntity> createNewBook(@RequestBody BookEntity bookEntity){
        System.out.println("Book is created with title: " + bookEntity.getTitle());
        BookEntity book =  bookService.createNewBook(bookEntity);
        return new ResponseEntity<>(book,HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{book_id}")
    public ResponseEntity<Boolean> deleteBookById(@PathVariable Long book_id){
        boolean isDeleted = bookService.deleteBookById(book_id);
        return ResponseEntity.ok(true);
    }

    @GetMapping(path = "/byTitle")
    @PreAuthorize("@SessionMapping.hasPlan(T(com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub).FREE)")
    public ResponseEntity<List<BookEntity>> findByTitle(@RequestParam String title){
        List<BookEntity> books = bookService.findByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/byDate")
    public ResponseEntity<List<BookEntity>> findByPublishedDate(@RequestParam LocalDate date){
        List<BookEntity> books = bookService.findByPublishedDate(date);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/byAuthorName")
    public ResponseEntity<List<BookEntity>> findByAuthorName(@RequestParam String name){
        List<BookEntity> books = bookService.findByAuthorName(name);
        return ResponseEntity.ok(books);
    }

    @PutMapping(path = "/{author_id}")
    public ResponseEntity<BookEntity> updateBook(@RequestBody BookEntity bookEntity, @PathVariable Long book_id){
        BookEntity book = bookService.updateBook(bookEntity,book_id);
        return ResponseEntity.ok(book);
    }

}

