package br.sdconecta.testebackend.model;

import br.sdconecta.testebackend.enums.AuthorizationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "userToken")
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String accessToken;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    private OffsetDateTime expiration;

}