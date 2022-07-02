package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInDto {

    private Long userIdChange;
    private String name;
    private String surname;
    private String password;

    @Email
    private String email;
    private List<CrmInDto> crms = new ArrayList<>();
    private String mobilePhone;
    private String profileType;

}
