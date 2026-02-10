package com.example.auth.auth_app_backend.security;

import com.example.auth.auth_app_backend.entities.User;
import com.example.auth.auth_app_backend.exceptions.CustomeResourceNotFoundException;
import com.example.auth.auth_app_backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= userRepository.findByEmail(username).orElseThrow(()->new BadCredentialsException("Invalid email or Password !!"));

       return user;
    }
}
