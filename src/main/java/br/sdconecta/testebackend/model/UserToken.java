package br.sdconecta.testebackend.model;

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
    @Column
    private Long tokenId;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    @Column
    private OffsetDateTime expiration;

}