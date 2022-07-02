package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileTypeDto {

    private Long profileTypeId;
    private ProfileType profileType;

}
