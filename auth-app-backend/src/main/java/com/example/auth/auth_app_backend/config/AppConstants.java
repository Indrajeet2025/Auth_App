package com.example.auth.auth_app_backend.config;



public class AppConstants
{

    public static  final String[] AUTH_PUBLIC_URLs={
            "/api/v1/auth/**",
            "/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**"
    };

    public static final String ADMIN_ROLE="ADMIN";
    public static final String GUEST_ROLE="GUEST";
}
