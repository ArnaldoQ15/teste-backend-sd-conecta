package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileTypeDto {

    private Long userIdChange;
    private ProfileType profileType;

}
