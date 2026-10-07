package com.harshpatil.com.springSecurity.Handler;

import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.repository.UserRepository;
import com.harshpatil.com.springSecurity.services.JwtService;
import com.harshpatil.com.springSecurity.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication;

        DefaultOAuth2User oAuthUser=(DefaultOAuth2User) token.getPrincipal();

        String email=(oAuthUser.getAttribute("email"));

        User user= userService.getUserByEmail(email);

        if(user==null)
        {
            User newUser=User.builder()
                    .name(oAuthUser.getAttribute("name"))
                    .email(email)
                    .build();

            user=userService.save(newUser);
        }

        String accessToken=jwtService.generateAccessToken(user);

        String refreshToken=jwtService.generateRefreshToken(user);

        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


        String frontEndUrl="http://localhost:8080/home.html?token="+accessToken;

        getRedirectStrategy().sendRedirect(request,response,frontEndUrl);

     }
}
