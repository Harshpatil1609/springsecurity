package com.harshpatil.com.springSecurity.controller;

import com.harshpatil.com.springSecurity.dto.PostDto;
import com.harshpatil.com.springSecurity.entities.PostEntity;
import com.harshpatil.com.springSecurity.services.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPost()
    {
        return ResponseEntity.ok(postService.getAllPost());

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id)
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        System.out.println("USER = " + authentication.getName());
        System.out.println("AUTHORITIES = " + authentication.getAuthorities());
        return  ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PostDto> createNewPost(@RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.creaNewPost(postDto), HttpStatus.CREATED);

    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id,@RequestBody PostDto postDto)
    {
        return ResponseEntity.ok(postService.updatePost(id,postDto));
    }


}
