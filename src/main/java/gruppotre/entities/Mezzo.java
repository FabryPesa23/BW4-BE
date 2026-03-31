package gruppotre.entities;

import gruppotre.enums.TipoVeicolo;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mezzo")
public class Mezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TipoVeicolo tipo;

    @Column(name = "capienza", nullable = false)
    private int capienza;

    @OneToMany(mappedBy = "mezzo")
    private List<StatoMezzo> stati;

    @OneToMany(mappedBy = "mezzo")
    private List<Percorrenza> percorrenzas;
}
