package com.example.auth.auth_app_backend.exceptions;


import com.example.auth.auth_app_backend.dtos.ApiError;
import com.example.auth.auth_app_backend.dtos.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    private final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(
            {
                    UsernameNotFoundException.class,
                    BadCredentialsException.class,
                    CredentialsExpiredException.class,
                    DisabledException.class
            }
    )
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request)
    {
        logger.info("Exception : {}",e.getClass().getName()
        );
       ApiError error= ApiError.of(HttpStatus.BAD_REQUEST.value(),"Bad Request",e.getMessage(),request.getRequestURI());
        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(CustomeResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(CustomeResourceNotFoundException customeResourceNotFoundException)
    {
       ErrorResponse errorResponse=new ErrorResponse(customeResourceNotFoundException.getMessage(), HttpStatus.NOT_FOUND,404);
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception)
    {
        ErrorResponse errorResponse=new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST,400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
