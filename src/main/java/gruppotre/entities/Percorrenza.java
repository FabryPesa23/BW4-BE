package gruppotre.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "percorrenza")
public class Percorrenza {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;

    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

    @Column(name = "data_partenza")
    private LocalDateTime dataPartenza;

    @Column(name = "tempo_effettivo")
    private int tempoEffettivo; // minuti
}
