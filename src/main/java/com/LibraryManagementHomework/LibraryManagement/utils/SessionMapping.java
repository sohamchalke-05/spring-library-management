package com.LibraryManagementHomework.LibraryManagement.utils;

import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Session_Sub.*;

@Component("SessionMapping")
public class SessionMapping {

    private static final Map<Session_Sub,Integer> map = Map.of(
            FREE,1,
            BASIC, 2,
            PREMIUM, 3
    );

    public static Integer getSessionCountLimit(Session_Sub sessionSub){
        return map.getOrDefault(sessionSub, 1);
    }

    public boolean hasPlan(Session_Sub requiredPlan) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userPlanStr = user.getSessionSub();
        Session_Sub userPlan = userPlanStr != null ? Session_Sub.valueOf(userPlanStr.toUpperCase()) : Session_Sub.FREE;

        return comparePlans(userPlan, requiredPlan);
    }

    private boolean comparePlans(Session_Sub userPlan, Session_Sub requiredPlan) {
        return map.get(userPlan) >= map.get(requiredPlan);
    }
}

