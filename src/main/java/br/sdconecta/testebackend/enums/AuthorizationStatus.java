package br.sdconecta.testebackend.enums;

import lombok.Getter;

@Getter
public enum AuthorizationStatus {

    WAITING_FOR_AUTHORIZATION,
    AUTHORIZED,
    REJECTED

}
