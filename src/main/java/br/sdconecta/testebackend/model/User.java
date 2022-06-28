package br.sdconecta.testebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "userSystem")
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

    @Column
    private String email;

    @Column
    private String mobilePhone;

    @Column
    private Boolean admin;

    @Column
    @Enumerated(EnumType.STRING)
    private AuthorizationStatus authorizationStatus;

    @OneToMany(mappedBy = "crmId", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Crm> crms = new ArrayList<>();

}