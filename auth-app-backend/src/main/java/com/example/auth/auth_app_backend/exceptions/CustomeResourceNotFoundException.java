package com.example.auth.auth_app_backend.exceptions;


public class CustomeResourceNotFoundException extends RuntimeException
{

    public CustomeResourceNotFoundException(String message)
    {
        super(message);
    }

    public CustomeResourceNotFoundException()
    {
        super("Resource Not Found !!");
    }
}
