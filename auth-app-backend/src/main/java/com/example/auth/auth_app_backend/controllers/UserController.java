package com.example.auth.auth_app_backend.controllers;

import com.example.auth.auth_app_backend.dtos.UserDto;
import com.example.auth.auth_app_backend.entities.User;
import com.example.auth.auth_app_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    // get all User api
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers()
    {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/email/{user_email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("user_email") String user_email)
    {
        return ResponseEntity.ok(userService.getUserByEmail(user_email));
    }

    @DeleteMapping("/{userId}")
    public void  deleteUser(@PathVariable("userId") String userId)
    {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public  ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable("userId") String userId)
    {
        return  ResponseEntity.ok(userService.updateUser(userDto,userId));
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId)
    {
        return ResponseEntity.ok(userService.getUserByid(userId));
    }
}
