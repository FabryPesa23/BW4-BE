package gruppotre.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import gruppotre.enums.TipoVeicolo;

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

    gruppotre.enums.TipoVeicolo tipo;

}
