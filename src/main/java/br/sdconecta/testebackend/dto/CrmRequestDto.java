package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.FederativeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;
import static br.sdconecta.testebackend.util.Constants.VERY_LONG;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmRequestDto {

    @NotBlank(message = NOT_BLANK)
    @Length(max = 45, message = VERY_LONG)
    private String crm;

    @NotBlank(message = NOT_BLANK)
    private FederativeUnit uf;

    private String specialty;

}
