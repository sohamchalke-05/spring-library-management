package com.LibraryManagementHomework.LibraryManagement.controller;

import com.LibraryManagementHomework.LibraryManagement.entities.AuthorEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.services.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "/{author_id}")
    public AuthorEntity getAuthorById(@PathVariable Long author_id){
        return authorService.getAuthorById(author_id);
    }

    @GetMapping
    public List<AuthorEntity> getAllAuthor(){
        return authorService.getAllAuthor();
    }

    @PostMapping
    public AuthorEntity createNewAuthor(@RequestBody AuthorEntity authorEntity){
        return authorService.createNewAuthor(authorEntity);
    }

    @DeleteMapping(path = "/{author_id}")
    public boolean deleteAuthorById(@PathVariable Long author_id){
        return authorService.deleteAuthorById(author_id);
    }

    @GetMapping(path = "/byName")
    public List<AuthorEntity> findByName(@RequestParam String name){
        return authorService.findByName(name);
    }

    @PutMapping(path = "/{author_id}")
    public AuthorEntity updateAuthor(@RequestBody AuthorEntity authorEntity,@PathVariable Long author_id){
        return authorService.updateAuthor(authorEntity,author_id);
    }

    @PutMapping(path = "/{author_id}/book/{book_id}")
    public AuthorEntity assignAuthorToBook(@PathVariable Long author_id,@PathVariable Long book_id){
        return authorService.assignAuthorToBook(author_id,book_id);
    }
}
