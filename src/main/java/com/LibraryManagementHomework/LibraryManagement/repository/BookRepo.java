package com.LibraryManagementHomework.LibraryManagement.repository;

import com.LibraryManagementHomework.LibraryManagement.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookRepo extends JpaRepository<BookEntity,Long> {

    List<BookEntity> findByTitle(String title);

    List<BookEntity> findByPublishedDateAfter(LocalDate date);

    List<BookEntity> findByAuthorName(String name);

}
