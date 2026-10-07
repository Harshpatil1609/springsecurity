package com.harshpatil.com.springSecurity.filters;

import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.repository.SessionRepository;
import com.harshpatil.com.springSecurity.services.JwtService;
import com.harshpatil.com.springSecurity.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.sql.rowset.spi.SyncResolver;
import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
//            it move the request to next filter
            }

            String token = requestTokenHeader.substring(7);
        System.out.println(token);

        try{
            String tokenType=jwtService.getTokenType(token);

            if(!"access".equals(tokenType))
            {
                filterChain.doFilter(request,response);
                return;
            }

            Long userid = jwtService.getUserIdFromToken(token);
            System.out.println(userid);

            if (userid != null && SecurityContextHolder.getContext().getAuthentication() == null) {


//            check is user is present in the db authention work here
                User user = userService.getUserById(userid);

//            authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

//            prevent the web form ddos attack and sotre more info about the authentication session like
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        }catch (Exception e)
        {
            handlerExceptionResolver.resolveException(request,response,null, e);
        }

//        with the response

    }
}
