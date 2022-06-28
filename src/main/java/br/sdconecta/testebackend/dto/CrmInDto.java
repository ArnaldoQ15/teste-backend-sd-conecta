package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.model.FederativeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrmInDto {

    private Long crmId;
    private String crm;
    private FederativeUnit uf;
    private String specialty;

}
