package com.harshpatil.com.springSecurity.dto;

import com.harshpatil.com.springSecurity.entities.enums.Permissions;
import com.harshpatil.com.springSecurity.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {

    private String email;
    private String pass;
    private String name;
    private Set<Role> roles;
    private Set<Permissions> permissions;
}
