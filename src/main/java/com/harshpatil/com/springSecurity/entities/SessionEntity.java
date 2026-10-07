package com.harshpatil.com.springSecurity.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name="user_id",unique = true)
    private Long userId;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String token;
}
