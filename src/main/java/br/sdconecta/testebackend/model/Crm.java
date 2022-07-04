package br.sdconecta.testebackend.model;

import br.sdconecta.testebackend.enums.FederativeUnit;
import lombok.*;

import javax.persistence.*;

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
    private String crm;

    @Column
    @Enumerated(EnumType.STRING)
    private FederativeUnit uf;

    @Column
    private String specialty;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

}
