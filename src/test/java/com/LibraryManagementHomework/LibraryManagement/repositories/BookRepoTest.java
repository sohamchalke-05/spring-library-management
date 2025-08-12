package com.LibraryManagementHomework.LibraryManagement.repositories;

import com.LibraryManagementHomework.LibraryManagement.TestContainerConfiguration;
import com.LibraryManagementHomework.LibraryManagement.entities.AuthorEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.repository.AuthorRepo;
import com.LibraryManagementHomework.LibraryManagement.repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestContainerConfiguration.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    private BookEntity bookEntity;

    private AuthorEntity authorEntity;

    @BeforeEach
    void setUp() {
         bookEntity = BookEntity.builder()
                .title("The Love")
                 .publishedDate(LocalDate.now())
                .price(200.0)
                .build();

         authorEntity = AuthorEntity.builder()
                .name("John Doe")
                 .build();
    }

    @Test
    void testFindByTitle_whenTitleIsPresent_thenReturnBookEntity(){

        //Given
        BookEntity book = bookRepo.save(bookEntity);

        //when
        List<BookEntity> bookEntities = bookRepo.findByTitle(book.getTitle());

        //then
        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isNotEmpty();
        assertThat(bookEntities.get(0).getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void testFindByTitle_whenTitleIsNotPresent_thenReturnEmptyList(){

        //Given
        String Title = "Unknown Book Title";

        //when
        List<BookEntity> bookEntities = bookRepo.findByTitle(Title);

        //then
        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isEmpty();

    }

    @Test
    void testFindByPublishedDateAfter_whenDateIsValid_thenReturnBookEntities(){
        //Given
        BookEntity book = bookRepo.save(bookEntity);

        //when
        List<BookEntity> bookEntities = bookRepo.findByPublishedDateAfter(book.getPublishedDate().minusDays(5));

        //then
        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isNotEmpty();
        assertThat(bookEntities.get(0).getPublishedDate()).isAfter(book.getPublishedDate().minusDays(5));
    }

    @Test
    void testFindByPublishedDateAfter_whenDateIsNotValid_thenReturnEmptyList(){
        //Given
        LocalDate date = LocalDate.now().plusDays(2);

        //when
        List<BookEntity> bookEntities = bookRepo.findByPublishedDateAfter(date);

        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isEmpty();
    }

    @Test
    void testFindByAuthorName_whenAuthorNameIsPresent_thenReturnBookEntities() {
        // Given
        AuthorEntity author = authorRepo.save(authorEntity);
        bookEntity.setAuthor(author);
        BookEntity book = bookRepo.save(bookEntity);

        // When
        List<BookEntity> bookEntities = bookRepo.findByAuthorName(book.getAuthor().getName());

        // Then
        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isNotEmpty();
        assertThat(bookEntities.get(0).getAuthor().getName()).isEqualTo(book.getAuthor().getName());
    }

    @Test
    void testFindByAuthorName_whenAuthorNameIsNotPresent_thenReturnEmptyList() {
        // Given
        String authorName = "Unknown Author";

        // When
        List<BookEntity> bookEntities = bookRepo.findByAuthorName(authorName);

        // Then
        assertThat(bookEntities).isNotNull();
        assertThat(bookEntities).isEmpty();
    }
}
