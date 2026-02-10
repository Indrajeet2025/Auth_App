package com.example.auth.auth_app_backend.services.impl;

import com.example.auth.auth_app_backend.config.AppConstants;
import com.example.auth.auth_app_backend.dtos.UserDto;
import com.example.auth.auth_app_backend.entities.Provider;
import com.example.auth.auth_app_backend.entities.Role;
import com.example.auth.auth_app_backend.entities.User;
import com.example.auth.auth_app_backend.exceptions.CustomeResourceNotFoundException;
import com.example.auth.auth_app_backend.helpers.UserHelper;
import com.example.auth.auth_app_backend.repositories.RoleRepository;
import com.example.auth.auth_app_backend.repositories.UserRepository;
import com.example.auth.auth_app_backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private  final UserRepository userRepository;
    private   final ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto)  {
        log.info("Create User called from userServiceImpl: {}", userDto);
       if(userDto.getEmail()==null|userDto.getEmail().isBlank())
       {
           throw new IllegalArgumentException("Email Required...");
       }
       if(userRepository.existsByEmail(userDto.getEmail()))
       {
           throw  new IllegalArgumentException("Email already exist...");
       }
        // extra checks
       User user= modelMapper.map(userDto, User.class);
       user.setProvider(userDto.getProvider()!=null?userDto.getProvider(): Provider.LOCAL);

       // Assign Role to new
        // assign default role
        Role role=roleRepository.findByRoleName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
        user.getRoles().add(role);


       User savedUser=userRepository.save(user);

       return modelMapper.map(savedUser,UserDto.class);

    }

    @Override
    public UserDto getUserByEmail(String email) {
       User user= userRepository.findByEmail(email).orElseThrow(()->new CustomeResourceNotFoundException("User Not found with this email !!"));

       return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uuid=UserHelper.parseUUID(userId);
        User existingUser=userRepository.findById(uuid).orElseThrow(()->new CustomeResourceNotFoundException("User Not Found with given id !!"));
        // we are not going to let update anyone email
        if(userDto.getUsername()!=null) existingUser.setEmail(userDto.getUsername());
        if(userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());
        // Todo change the Password Updation Login
        if(userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnabled(userDto.isEnabled());
        existingUser.setUpdatedAt(Instant.now());

       User updatedUser= userRepository.save(existingUser);
       return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId)
    {
      UUID uuid= UserHelper.parseUUID(userId);
      User user=userRepository.findById(uuid).orElseThrow(()->new CustomeResourceNotFoundException("User Not Found with given id !!"));

      userRepository.delete(user);
    }

    @Override
    public UserDto getUserByid(String userId) {
        User user=userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(()-> new CustomeResourceNotFoundException("User Not Found with given id !!"));
        return modelMapper.map(user,UserDto.class);

    }

    @Override
    @Transactional()
    public Iterable<UserDto> getAllUser() {
        return  userRepository.findAll().stream().map(user -> modelMapper.map(user,UserDto.class)).toList();
    }




}
