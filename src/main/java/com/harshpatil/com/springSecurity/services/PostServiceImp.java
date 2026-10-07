package com.harshpatil.com.springSecurity.services;

import com.harshpatil.com.springSecurity.dto.PostDto;
import com.harshpatil.com.springSecurity.entities.PostEntity;
import com.harshpatil.com.springSecurity.entities.User;
import com.harshpatil.com.springSecurity.exception.ResourceNotFoundException;
import com.harshpatil.com.springSecurity.repository.PostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDto> getAllPost() {

        List<PostEntity> postEntityList = postRepository.findAll();

        return postEntityList
                .stream()
                .map(postEntity -> modelMapper.map(postEntity,PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto creaNewPost(PostDto postDto) {
        PostEntity entityToSave =modelMapper.map(postDto,PostEntity.class);
        PostEntity savedEntity=postRepository.save(entityToSave);
        return modelMapper.map(savedEntity,PostDto.class);
    }

    @Override
    public PostDto getPostById(Long id) {
        isExisted(id);
//
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("user {}",user);

        PostEntity postEntity=postRepository.findById(id).orElseThrow();
        return modelMapper.map(postEntity,PostDto.class);
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto) {
        isExisted(id);
        PostEntity postToUpdate=postRepository.findById(id).orElseThrow();
        postDto.setId(id);
        modelMapper.map(postDto,postToUpdate);

        PostEntity savedEntity=postRepository.save(postToUpdate);

        return  modelMapper.map(savedEntity,PostDto.class);
    }

    public boolean isExisted(Long id)
    {
        boolean exists=postRepository.existsById(id);

        if(!exists)
        {
            throw new ResourceNotFoundException("post is not found with id : "+id);

        }
        return true;
    }
}
