package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.entities.SessionEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub;
import com.LibraryManagementHomework.LibraryManagement.repository.SessionRepository;
import com.LibraryManagementHomework.LibraryManagement.utils.SessionMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub.*;


@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public void generateNewSession(User user,String refreshToken){
        List<SessionEntity> allSessions = sessionRepository.findAllByUser(user);

        String sessionSubString = user.getSessionSub();
        Session_Sub sessionSub = sessionSubString != null
                ? Session_Sub.valueOf(sessionSubString.toUpperCase())
                : Session_Sub.FREE;

        if(allSessions.size() == SessionMapping.getSessionCountLimit(sessionSub)){
            allSessions.sort(Comparator.comparing(SessionEntity::getRecentlyUsed));
            sessionRepository.delete(allSessions.get(0));
        }

        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);

    }

    public boolean isValid(String refreshToken){
        System.out.println("Entered in isValid()");
        SessionEntity sessionEntity = sessionRepository.findByRefreshToken(refreshToken);
        return sessionEntity != null;
    }

    public void logoutSession(User user){
        SessionEntity sessionEntity = sessionRepository.findByUser(user);
        sessionRepository.delete(sessionEntity);
    }

    public void delete(String oldRefreshToken) {
        SessionEntity session = sessionRepository.findByRefreshToken(oldRefreshToken);
        if (session != null) {
            System.out.println("Deleting session with ID: " + session.getSessionId());
            sessionRepository.delete(session);
        } else {
            System.out.println("No session found with token: " + oldRefreshToken);
        }
    }

    public void save(User user, String newRefreshToken) {
        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .build();
        sessionRepository.save(newSession);
    }
}
