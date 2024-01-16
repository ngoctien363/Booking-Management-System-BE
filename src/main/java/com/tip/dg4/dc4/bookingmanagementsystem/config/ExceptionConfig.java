package com.tip.dg4.dc4.bookingmanagementsystem.config;

import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.BadRequestException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.NotFoundException;
import com.tip.dg4.dc4.bookingmanagementsystem.exceptions.UnauthorizedException;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.constants.ExceptionConstant;
import com.tip.dg4.dc4.bookingmanagementsystem.shared.res.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionConfig{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                httpStatus.getReasonPhrase(),
                ex.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                httpStatus.getReasonPhrase(),
                ex.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            httpStatus.getReasonPhrase(),
            ExceptionConstant.USER_E002
        );

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            httpStatus.getReasonPhrase(),
            ExceptionConstant.USER_E009
        );
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> listError = new ArrayList<>();
        StringBuilder errorResponses = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            listError.add(error.getDefaultMessage());
        }
        );

        for(int i=0; i<listError.size();i++){
            errorResponses.append(listError.get(i));
            while (i < listError.size()-1){
                errorResponses.append("\n");
                break;
            }
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            httpStatus.getReasonPhrase(),
            errorResponses.toString()
        );

    return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponse> handleSignatureException(SignatureException ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            httpStatus.getReasonPhrase(),
            ex.getMessage()
        );
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            LocalDateTime.now(),
            httpStatus.getReasonPhrase(),
            ex.getMessage()
        );
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                httpStatus.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}
