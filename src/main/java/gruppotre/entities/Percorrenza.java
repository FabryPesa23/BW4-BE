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

    @Column(name = "data_partenza", nullable = false)
    private LocalDateTime dataPartenza;

    @Column(name = "tempo_effettivo")
    private int tempoEffettivo; // minuti

    public static void main(String[] args) {

    }
    public Percorrenza() {

    }

    public Percorrenza(Mezzo mezzo, Tratta tratta, LocalDateTime dataPartenza) {
        this.mezzo = mezzo;
        this.tratta = tratta;
        this.dataPartenza = dataPartenza;
        this.tempoEffettivo = calcoloTempo();
    }

    private int calcoloTempo() {
        int tempo = tratta.getTempoPrevisto();

        switch (mezzo.getTipo()) {
            case BUS -> tempo += 0;
            case TRAM -> tempo -= 10;
        }

        return tempo;
    }

//    GETTER-
    public UUID getId() {
        return id;
    }

    public int getTempoEffettivo() {
        return tempoEffettivo;
    }

    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public Tratta getTratta() {
        return tratta;
    }

}
