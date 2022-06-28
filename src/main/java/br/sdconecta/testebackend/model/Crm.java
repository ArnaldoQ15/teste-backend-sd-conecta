package br.sdconecta.testebackend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Crm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long crmId;

    @Column
    @Size(max = 45)
    private String crm;

    @Column
    @Enumerated(EnumType.STRING)
    private FederativeUnit uf;

    @Column
    private String specialty;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

}
