package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.TestContainerConfiguration;
import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import com.LibraryManagementHomework.LibraryManagement.repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    private BookEntity bookEntity;

    @BeforeEach
    void setUp() {
         bookEntity = BookEntity.builder()
                 .id(1L)
                .title("Test Book")
                .publishedDate(LocalDate.now())
                .build();

    }

    @Test
    void testFindById_whenBookExists_thenReturnBook(){
        //assign

        Long bookId = 1L;
        when(bookRepo.findById(bookId)).thenReturn(Optional.of(bookEntity));

        //act
        BookEntity book = bookService.getBookById(bookId);

        //assert
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(bookEntity.getId());
        assertThat(book.getTitle()).isEqualTo(bookEntity.getTitle());

        verify(bookRepo,only()).findById(bookId);
    }

    @Test
    void testFindById_whenBookDoesNotExist_thenReturnNull(){
        //assign
        Long bookId = 2L;
        when(bookRepo.findById(bookId)).thenReturn(Optional.empty());

        //act
        BookEntity book = bookService.getBookById(bookId);

        //assert
        assertThat(book).isNull();
    }

    @Test
    void testGetBookById_ExceptionThrown() {
        // Arrange
        Long bookId = 5L;
        when(bookRepo.findById(bookId)).thenThrow(new RuntimeException("Error fetching book"));

        // Act & Assert
        assertThatThrownBy(()->bookService.getBookById(bookId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error fetching book");
    }

    @Test
    void testCreateNewBook_whenBookIsValid_thenReturnSavedBook(){
        //assign
        when(bookRepo.save(any(BookEntity.class))).thenReturn(bookEntity);

        //act
        BookEntity book = bookService.createNewBook(bookEntity);

        //assert
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(bookEntity.getId());
        assertThat(book.getTitle()).isEqualTo(bookEntity.getTitle());
    }

    @Test
    void testCreateNewBook_whenBookIsInvalid_thenThrowException(){
        //assign
        when(bookRepo.save(bookEntity)).thenThrow(new RuntimeException("Book creation failed"));

        //act and assert
        assertThatThrownBy(()->bookService.createNewBook(bookEntity))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book creation failed");
    }

    @Test
    void testDeleteBookById_whenBookExists_thenReturnTrue(){
        //assign
        Long bookId = 1L;
        when(bookRepo.existsById(bookId)).thenReturn(true);
        bookRepo.deleteById(bookId);

        //act
        boolean isDeleted = bookService.deleteBookById(bookId);

        //assert
        assertThat(isDeleted).isTrue();
    }

    @Test
    void testDeleteBookById_whenBookNotExists_thenReturnFalse(){
        //assign
        Long bookId = 2L;
        when(bookRepo.existsById(bookId)).thenReturn(false);

        //act
        boolean isDeleted = bookService.deleteBookById(bookId);

        //assert
        assertThat(isDeleted).isFalse();
        verify(bookRepo,never()).deleteById(bookId);
    }

    @Test
    void testDeleteBookById_ThrowException(){
        //assign
        Long bookId = 2L;
        when(bookRepo.existsById(bookId)).thenThrow(new RuntimeException("Error deleting book"));

        //act and assert
        assertThatThrownBy(()-> bookService.deleteBookById(bookId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error deleting book");
    }

    @Test
    void testGetAllBook_whenNoException_thenReturnListOfBooks(){
        //assign
        List<BookEntity> book = new ArrayList<>();
        book.add(bookEntity);
        when(bookRepo.findAll()).thenReturn(book);

        //act
        List<BookEntity> bookEntities = bookService.getAllBooks();

        //assert
        assertThat(bookEntities).isEqualTo(book);
    }

    @Test
    void testGetAllBook_whenBookNotExist_thenThrowException(){
        //assign
        List<BookEntity> book = new ArrayList<>();
        when(bookRepo.findAll()).thenThrow(new RuntimeException("Error fetching book list"));

        //act and assert
        assertThatThrownBy(() -> bookService.getAllBooks())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error fetching book list");
    }
}
