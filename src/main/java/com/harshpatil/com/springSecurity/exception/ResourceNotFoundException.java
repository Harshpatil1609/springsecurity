package com.harshpatil.com.springSecurity.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String name)
    {
        super(name);
    }
}
