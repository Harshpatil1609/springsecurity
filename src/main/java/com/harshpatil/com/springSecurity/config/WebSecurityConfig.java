package com.harshpatil.com.springSecurity.config;

import com.harshpatil.com.springSecurity.Handler.OAuth2SuccessHandler;
import com.harshpatil.com.springSecurity.entities.enums.Permissions;
import com.harshpatil.com.springSecurity.filters.JwtAuthFilter;
import com.harshpatil.com.springSecurity.filters.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.harshpatil.com.springSecurity.entities.enums.Role.ADMIN;
import static com.harshpatil.com.springSecurity.entities.enums.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] publicRoutes={"/auth/**","/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
    {
        httpSecurity
//                .formLogin(formLoginConfig-> formLoginConfig   this is old way to do it nowdays use the jwt topkens
//                        .loginPage("/newLogin.html"))
//                provides the default login page that is used when we add the dependency
                .authorizeHttpRequests(auth ->auth
//                         public routes
                        .requestMatchers(publicRoutes).permitAll()
                                .requestMatchers(HttpMethod.GET,"/post/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/post/**").hasAnyRole(ADMIN.name(),USER.name())

                                .requestMatchers(HttpMethod.POST,"/post/**")
                                .hasAuthority(Permissions.POST_CREATE.name())
                                .requestMatchers(HttpMethod.GET,"/post/**")
                                .hasAuthority(Permissions.POST_VIEW.name())

                                .requestMatchers(HttpMethod.PUT,"/post/**")
                                .hasAuthority(Permissions.POST_UPDATE.name())

                                .requestMatchers(HttpMethod.DELETE,"/post/**")
                            .hasAuthority(Permissions.POST_DELETE.name())

//                        .requestMatchers("/post/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )

            .sessionManagement(sessionCofig->sessionCofig
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)

//                register the custome security filter
//                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler));



//                .formLogin(Customizer.withDefaults());


        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService myInMemoryUserDetailService()
//    {
//        UserDetails normalUser= User
//                .withUsername("harsh")
//                .password(passwordEncoder().encode("Harsh123"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser= User
//                .withUsername("raj")
//                .password(passwordEncoder().encode("Raj123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser,adminUser);
//    }


}
