package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.model.AuthorizationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOutDto {

    private Long userId;
    private String name;
    private String surname;
    private String email;
    private String mobilePhone;
    private Boolean admin;
    private List<CrmOutDto> crms;
    private AuthorizationStatus authorizationStatus;

}
