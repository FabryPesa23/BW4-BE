package gruppotre.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tratta")
public class Tratta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "zona_part", nullable = false)
    private String zonaPartenza;

    @Column(nullable = false)
    private String capolinea;

    @Column(name = "tempo_previsto", nullable = false)
    private int tempoPrevisto;

    @OneToMany(mappedBy = "tratta")
    private List<Percorrenza> percorrenza;
}
