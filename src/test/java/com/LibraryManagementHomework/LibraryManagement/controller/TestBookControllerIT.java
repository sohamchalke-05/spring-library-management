package com.LibraryManagementHomework.LibraryManagement.controller;

import com.LibraryManagementHomework.LibraryManagement.TestContainerConfiguration;
import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
@Import({TestContainerConfiguration.class})
public class TestBookControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BookRepo bookRepo;

    private BookEntity bookEntity;

    @BeforeEach
    void setup() {
        bookRepo.deleteAll();

        bookEntity = BookEntity.builder()
                .title("The Love")
                .publishedDate(LocalDate.now())
                .price(200.0)
                .build();
    }

    @Test
    void testGetBookById_success() {
        BookEntity savedBook = bookRepo.save(bookEntity);

        webTestClient
                .get()
                .uri("/book/" + savedBook.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedBook.getId().intValue())
                .jsonPath("$.title").isEqualTo(savedBook.getTitle());
    }


    @Test
    void testGetAllBooks_success() {
        bookRepo.save(bookEntity);

        webTestClient
                .get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookEntity.class)
                .hasSize(1);
    }

    @Test
    void testGetAllBooks_empty() {
        webTestClient
                .get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookEntity.class)
                .hasSize(0);
    }

    @Test
    void testCreateNewBook_success() {
        webTestClient
                .post()
                .uri("/book")
                .bodyValue(bookEntity)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo(bookEntity.getTitle())
                .jsonPath("$.price").isEqualTo(bookEntity.getPrice());
    }
}


