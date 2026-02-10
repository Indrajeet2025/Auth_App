package com.example.auth.auth_app_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth Application build by Indrajeet Joshi !",
                description = "Generic Auth app that can be used with any application",
                contact = @Contact(
                        name = "Indrajeet Joshi",
                        url = "github.com/Indrajeet2025",
                        email = "joshiindrajeet09@gmail.com"

                ),
                version = "1.0",
                summary = "This app is very usefull Auth app,you can directly clone it and start development"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)


@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",// Authorization:Bearer
        bearerFormat = "JWT"
)
public class ApiDocConfig
{


}
