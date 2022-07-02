package br.sdconecta.testebackend.model;

import br.sdconecta.testebackend.enums.AuthorizationStatus;
import br.sdconecta.testebackend.enums.ProfileType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "userSystem")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    private String mobilePhone;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    @OneToMany(mappedBy = "crmId", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Crm> crms = new ArrayList<>();

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tokenId")
    private UserToken token;

}