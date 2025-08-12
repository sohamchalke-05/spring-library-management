package com.LibraryManagementHomework.LibraryManagement.repository;

import com.LibraryManagementHomework.LibraryManagement.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<AuthorEntity,Long> {

    List<AuthorEntity> findByName(String name);

}
