package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static br.sdconecta.testebackend.util.Constants.EMAIL_INVALID;
import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = NOT_BLANK)
    private String password;

}
