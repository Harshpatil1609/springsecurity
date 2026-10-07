package com.harshpatil.com.springSecurity.services;

import com.harshpatil.com.springSecurity.dto.LoginDto;
import com.harshpatil.com.springSecurity.dto.SignUpDto;
import com.harshpatil.com.springSecurity.dto.UserDto;
import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.entities.enums.Role;
import com.harshpatil.com.springSecurity.exception.ResourceNotFoundException;
import com.harshpatil.com.springSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("username used in loaduserByname "+username);
        return userRepository.findByEmail(username)
                .orElseThrow(()->new BadCredentialsException("user with email : "+username+" not found"));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());

        if (user.isPresent()) {
            throw new BadCredentialsException("User with email already exists " + signUpDto.getEmail());
        }

        User toBeCreate = modelMapper.map(signUpDto, User.class);
        toBeCreate.setPassword(passwordEncoder.encode(signUpDto.getPass()));

//        toBeCreate.setRoles(signUpDto.getRoles());

        User savedUser = userRepository.save(toBeCreate);

        return modelMapper.map(savedUser, UserDto.class);
    }

    public User getUserById(Long id)
    {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with id : "+id + " not found"));
    }

    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email).orElse(null);
    }


    public User save(User newUser) {

       return userRepository.save(newUser);
    }
}
