package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import static br.sdconecta.testebackend.util.Constants.EMAIL_INVALID;
import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotNull(message = NOT_BLANK)
    private Long userIdChange;

    private String name;

    private String surname;

    private String password;

    @Email(message = EMAIL_INVALID)
    private String email;

    private String mobilePhone;

    private String profileType;

}
