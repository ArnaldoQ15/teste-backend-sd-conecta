package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.FederativeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static br.sdconecta.testebackend.util.Constants.VERY_LONG;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmUpdateDto {

    @Length(max = 45, message = VERY_LONG)
    private String crm;

    private FederativeUnit uf;

    private String specialty;

}
