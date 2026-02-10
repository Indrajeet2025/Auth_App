package com.example.auth.auth_app_backend.config;


import com.example.auth.auth_app_backend.dtos.ApiError;
import com.example.auth.auth_app_backend.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Arrays;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class SecurityConfig
{


    private  final JwtAuthenticationFilter jwtAuthenticationFilter;
    private  final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final ObjectMapper objectMapper;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests->
                authorizeHttpRequests
                        .requestMatchers(AppConstants.AUTH_PUBLIC_URLs).permitAll()
                        .requestMatchers(HttpMethod.GET).hasRole(AppConstants.GUEST_ROLE)
                        .requestMatchers("/api/v1/users/**").hasRole(AppConstants.ADMIN_ROLE)
                        .anyRequest()
                        .authenticated())
                .oauth2Login(oauth2->{
                                oauth2.successHandler(authenticationSuccessHandler)
                                        .failureHandler(null);

                        }
                        )
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex->ex.authenticationEntryPoint(((request, response, authException) ->
                             {
                                    //send error message to client
                                 authException.printStackTrace();
                                 response.setStatus(401);
                                 response.setContentType("application/json");
                                 String message=" Unauthorized access! "+authException.getMessage();
                                 String error=(String) request.getAttribute("error");

                                 if(error!=null)
                                 {
                                     message=error;
                                 }

                                 Map<String,Object> errorMap=Map.of("message",message,"statusCode",401);
                                 var objectMapper=new ObjectMapper();
                                 response.getWriter().write(objectMapper.writeValueAsString(errorMap));
                             }
                        )

                        )
                        .accessDeniedHandler((request, response, e) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");

                            String message = e.getMessage();
                            String error = (String) request.getAttribute("error");
                            if (error != null) message = error;

                            ApiError apiError = ApiError.of(
                                    HttpStatus.FORBIDDEN.value(),
                                    "Forbidden Access",
                                    message,
                                    request.getRequestURI()
                            );

                            response.getWriter().write(objectMapper.writeValueAsString(apiError));
                        })

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return  configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.frontend_url}") String corsUrls
    ){
       String []urls= corsUrls.trim().split(",");
        var config= new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(urls));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));

        var source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }




}
