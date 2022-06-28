package br.sdconecta.testebackend.exception;

import br.sdconecta.testebackend.dto.ExceptionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> NotFoundException(NotFoundException exception){
        return ResponseEntity.status(NOT_FOUND).body(
                new ExceptionDto(NOT_FOUND, exception.getMessage(), OffsetDateTime.now())
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionDto> UnauthorizedException(UnauthorizedException exception){
        return ResponseEntity.status(UNAUTHORIZED).body(
                new ExceptionDto(UNAUTHORIZED, exception.getMessage(), OffsetDateTime.now())
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> BadRequestException(BadRequestException exception){
        return ResponseEntity.status(BAD_REQUEST).body(
                new ExceptionDto(BAD_REQUEST, exception.getMessage(), OffsetDateTime.now())
        );
    }

}