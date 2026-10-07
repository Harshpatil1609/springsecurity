package com.harshpatil.com.springSecurity.controller;


import com.harshpatil.com.springSecurity.dto.LoginDto;
import com.harshpatil.com.springSecurity.dto.LoginResponseDto;
import com.harshpatil.com.springSecurity.dto.SignUpDto;
import com.harshpatil.com.springSecurity.dto.UserDto;
import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.services.AuthService;
import com.harshpatil.com.springSecurity.services.SessionService;
import com.harshpatil.com.springSecurity.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto)
    {
        UserDto userDto=userService.signUp(signUpDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<LoginResponseDto> signIn(@RequestBody LoginDto loginDto , HttpServletRequest request, HttpServletResponse response)
    {

        LoginResponseDto loginResponseDto=authService.signIn(loginDto);

        Cookie cookie=new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest httpServletRequest)
    {
        Cookie[] cookies= httpServletRequest.getCookies();

        String refreshToken=null;
        for(Cookie cookie:cookies)
        {
            if("refreshToken".equals(cookie.getName()))
            {
                refreshToken=cookie.getValue();
            }

        }

        if (refreshToken == null) {
            throw new RuntimeException("Refresh token not found");
        }

        log.info("we are in refresh token");

        LoginResponseDto loginResponseDto=authService.getRefreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<@NonNull String> logout(HttpServletRequest request,HttpServletResponse response)
    {
        Cookie[] cookies=request.getCookies();

        String refreshToken=null;

        if(cookies != null)
        {
            for (Cookie cookie:cookies)
            {
                if("refreshToken".equals(cookie.getName()))
                {
                    refreshToken=cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken != null)
        {
            sessionService.logout(refreshToken);
        }

        Cookie cookie=new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }

}
