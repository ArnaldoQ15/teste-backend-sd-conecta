package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static br.sdconecta.testebackend.util.Constants.EMAIL_INVALID;
import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInDto {

    private Long userIdChange;

    @NotBlank(message = NOT_BLANK)
    private String name;

    private String surname;

    @NotBlank(message = NOT_BLANK)
    private String password;

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotEmpty(message = NOT_BLANK)
    @Valid
    private List<CrmInDto> crms = new ArrayList<>();

    private String mobilePhone;

    private String profileType;

}
