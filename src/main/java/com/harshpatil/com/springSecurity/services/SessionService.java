package com.harshpatil.com.springSecurity.services;

import com.harshpatil.com.springSecurity.entities.Session;
import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT =2;

    public void generateNewSession(User user,String refreshToken)
    {
        List<Session> userSession =sessionRepository.findByUser(user);

        if(userSession.size() == SESSION_LIMIT)
        {
            userSession.sort(Comparator.comparing(Session::getLastUsedToken));

            Session leastRecentlyUsedSession=userSession.getFirst();

            sessionRepository.delete(leastRecentlyUsedSession);

        }

        Session newSession=Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .lastUsedToken(LocalDateTime.now())
                .build();

        sessionRepository.save(newSession);
    }


    public void validateSession(String refreshToken)
    {
        Session session= sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new
                SessionAuthenticationException("session not found for refreshToken "+refreshToken));

        session.setLastUsedToken(LocalDateTime.now());

        sessionRepository.save(session);

        System.out.println("Session ID: " + session.getId());
        System.out.println("Last used token: " + session.getLastUsedToken());
    }

    @Transactional
    public void logout(String refreshToken)
    {
        Session session=sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("session not found "));

        sessionRepository.deleteByRefreshToken(refreshToken);
    }
}
