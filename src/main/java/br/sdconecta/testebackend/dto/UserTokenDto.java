package br.sdconecta.testebackend.dto;

import br.sdconecta.testebackend.enums.AuthorizationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDto {

    private String accessToken;
    private String refreshToken;
    private AuthorizationStatus authorizationStatus;
    private OffsetDateTime expiration;

}
