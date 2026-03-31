package gruppotre.entities;

import gruppotre.enums.StatoVeicolo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "stato_mezzo")
public class StatoMezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Mezzo mezzo;

    @Enumerated(EnumType.STRING)
    private StatoVeicolo stato;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;
}
