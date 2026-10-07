package com.harshpatil.com.springSecurity.repository;

import com.harshpatil.com.springSecurity.entities.Session;
import com.harshpatil.com.springSecurity.entities.SessionEntity;
import com.harshpatil.com.springSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByUser(User user);


    Optional<Session> findByRefreshToken(String refreshToken);

//    Optional<SessionEntity> findByUserId(Long userId);
//
//    Optional<SessionEntity> findByToken(String token);
//
//    Optional<SessionEntity> findByUserIdAndToken(Long userId,String token);
//
//    void deleteByToken(String token);

    void deleteByRefreshToken(String refreshToken);



}
