package br.sdconecta.testebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {

    private String email;
    private String name;
    private String surname;
    private List<CrmRequestDto> crms;
    private String mobilePhone;

}
