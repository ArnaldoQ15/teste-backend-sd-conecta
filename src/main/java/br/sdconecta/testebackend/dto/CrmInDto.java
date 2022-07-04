package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.FederativeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;
import static br.sdconecta.testebackend.util.Constants.VERY_LONG;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmInDto {

    private Long crmId;

    private Long userId;

    @NotBlank(message = NOT_BLANK)
    @Length(max = 45, message = VERY_LONG)
    private String crm;

    @NotNull(message = NOT_BLANK)
    private FederativeUnit uf;

    private String specialty;

}