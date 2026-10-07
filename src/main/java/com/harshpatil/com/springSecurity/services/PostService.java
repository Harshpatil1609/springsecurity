package com.harshpatil.com.springSecurity.services;

import com.harshpatil.com.springSecurity.dto.PostDto;

import java.util.List;

public interface PostService {

    List<PostDto> getAllPost();

    PostDto creaNewPost(PostDto postDto);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);
}
