package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ExceptionDto {

    private HttpStatus status;
    private String message;
    private OffsetDateTime dateTime;

}
