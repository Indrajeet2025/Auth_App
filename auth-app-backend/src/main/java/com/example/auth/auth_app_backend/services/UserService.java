package com.example.auth.auth_app_backend.services;

import com.example.auth.auth_app_backend.dtos.UserDto;
import com.example.auth.auth_app_backend.entities.User;

public interface UserService
{
    // create user
   UserDto createUser(UserDto userDto);
    // get user by email
    UserDto getUserByEmail(String email);
    // update user
    UserDto updateUser(UserDto userDto,String userId);

    // delete user
    void deleteUser(String userId);

    // get user by id
    UserDto getUserByid(String userId);

    // get all users
    Iterable<UserDto> getAllUser();


}
