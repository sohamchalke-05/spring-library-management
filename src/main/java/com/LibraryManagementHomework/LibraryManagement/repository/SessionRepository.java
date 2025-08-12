package com.LibraryManagementHomework.LibraryManagement.repository;



import com.LibraryManagementHomework.LibraryManagement.entities.SessionEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity,Long> {

    List<SessionEntity> findAllByUser(User user);

    SessionEntity findByRefreshToken(String refreshToken);

    SessionEntity findByUser(User user);

    void deleteByRefreshToken(String refreshToken);
}
