package com.harshpatil.com.springSecurity.utils;

import com.harshpatil.com.springSecurity.entities.enums.Permissions;
import com.harshpatil.com.springSecurity.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.harshpatil.com.springSecurity.entities.enums.Permissions.*;
import static com.harshpatil.com.springSecurity.entities.enums.Role.*;

public class PermissionMapping {

    private static final Map<Role, Set<Permissions>> map= Map.of(
            USER,Set.of(USER_VIEW,POST_VIEW),
            CREATOR,Set.of(USER_VIEW,POST_VIEW, USER_UPDATE,POST_UPDATE),
            ADMIN,Set.of(USER_VIEW,POST_VIEW, USER_UPDATE,POST_UPDATE,USER_DELETE,USER_CREATE,POST_DELETE)
    );

    public static Set<SimpleGrantedAuthority> getAuthorities(Role role)
    {
        return map.get(role).stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet());
    }
}
