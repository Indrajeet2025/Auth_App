package com.example.auth.auth_app_backend.services;

import com.example.auth.auth_app_backend.dtos.UserDto;

public interface AuthService
{
    // register
    UserDto registerUser(UserDto userDto);

}
