package com.harshpatil.com.springSecurity.services;

import com.harshpatil.com.springSecurity.dto.LoginDto;
import com.harshpatil.com.springSecurity.dto.LoginResponseDto;
import com.harshpatil.com.springSecurity.entities.SessionEntity;
import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.repository.SessionRepository;
import com.harshpatil.com.springSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;

    public LoginResponseDto signIn(LoginDto loginDto) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPass())

        );

        User user= (User) authentication.getPrincipal();
//        contain all the data about the user

//        generating token
        String accessToken=jwtService.generateAccessToken(user);

        String refreshToken=jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user,refreshToken);

        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }


    public LoginResponseDto getRefreshToken(String refreshToken) {

        Long userId=jwtService.getUserIdFromToken(refreshToken);

        sessionService.validateSession(refreshToken);

        User user=userService.getUserById(userId);

        String accessToken=jwtService.generateAccessToken(user);

        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }
}
