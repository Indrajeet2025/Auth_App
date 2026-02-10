package com.example.auth.auth_app_backend.dtos;

import com.example.auth.auth_app_backend.entities.Provider;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {

    private UUID id;


    private String name;

    private String email;

    // ❌ Usually DO NOT expose password in DTO
    // include only if absolutely required (e.g. signup)
    private String password;

    private String image;

    @Builder.Default
    private boolean enabled = true;

    private Instant createdAt;

    private Instant updatedAt;

    @Builder.Default
    private Provider provider = Provider.LOCAL;

    // Send only role names, not Role entity
    private Set<RoleDto> roles=new HashSet<>();



    public String getUsername() { return this.email; }
}

