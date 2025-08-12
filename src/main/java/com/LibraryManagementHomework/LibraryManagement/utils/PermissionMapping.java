package com.LibraryManagementHomework.LibraryManagement.utils;

import com.LibraryManagementHomework.LibraryManagement.entities.enums.Permissions;
import com.LibraryManagementHomework.LibraryManagement.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;

import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Permissions.*;
import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Role.*;

public class PermissionMapping {

    private static final Map<Role, Set<Permissions>> map = Map.of(
            USER,Set.of(POST_VIEW,USER_VIEW),
            ADMIN,Set.of(POST_CREATE,POST_UPDATE,USER_UPDATE,POST_DELETE,USER_DELETE,USER_CREATE),
            CREATOR,Set.of(POST_CREATE,POST_UPDATE,USER_UPDATE)
    );

    public static Set<SimpleGrantedAuthority> grantedAuthorityWithRole(Role role){
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(java.util.stream.Collectors.toSet());
    }
}
