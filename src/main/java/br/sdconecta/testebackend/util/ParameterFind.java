package br.sdconecta.testebackend.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParameterFind {

    private Integer page;
    private Integer size;
    private String name;
    private String specialty;

}
