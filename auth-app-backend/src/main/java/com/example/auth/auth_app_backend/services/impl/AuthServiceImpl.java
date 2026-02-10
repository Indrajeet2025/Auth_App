package com.example.auth.auth_app_backend.services.impl;

import com.example.auth.auth_app_backend.config.AppConstants;
import com.example.auth.auth_app_backend.dtos.UserDto;
import com.example.auth.auth_app_backend.entities.Role;
import com.example.auth.auth_app_backend.repositories.RoleRepository;
import com.example.auth.auth_app_backend.services.AuthService;
import com.example.auth.auth_app_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService
{

    private final UserService userService;


    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDto registerUser(UserDto userDto) {

        //logic
        //verify email
        //verify password

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

       UserDto  userDto1= userService.createUser(userDto);
      return userDto1;
    }
}
