package com.harshpatil.com.springSecurity.entities;

import com.harshpatil.com.springSecurity.entities.enums.Permissions;
import com.harshpatil.com.springSecurity.entities.enums.Role;
import com.harshpatil.com.springSecurity.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

//    roles
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

//    ?Permission
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(
//            name = "user_permissions",
//            joinColumns = @JoinColumn(name = "user_id")
//    )
//    @Column(name = "permissions")
//    @Enumerated(EnumType.STRING)
//    private Set<Permissions> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

         Set<SimpleGrantedAuthority>authorities = new HashSet<>();

         roles.forEach(
                 role -> {
                     Set<SimpleGrantedAuthority> permissions= PermissionMapping.getAuthorities(role);
                     authorities.addAll(permissions);
                     authorities.add(new SimpleGrantedAuthority("ROLE_"+roles));
                 }
         );

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    public void set() {
    }
}

