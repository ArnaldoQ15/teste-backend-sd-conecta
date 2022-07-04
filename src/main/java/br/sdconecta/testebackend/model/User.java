package br.sdconecta.testebackend.model;

import br.sdconecta.testebackend.enums.AuthorizationStatus;
import br.sdconecta.testebackend.enums.ProfileType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static br.sdconecta.testebackend.util.Constants.EMAIL_INVALID;
import static br.sdconecta.testebackend.util.Constants.NOT_BLANK;

@Data
@Entity
@Table(name = "userSystem")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = NOT_BLANK)
    private String name;

    private String surname;

    @NotBlank(message = NOT_BLANK)
    private String password;

    @NotBlank(message = NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String email;

    private String mobilePhone;

    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    @OneToMany(mappedBy = "crmId", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Crm> crms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tokenId")
    private UserToken token;

}