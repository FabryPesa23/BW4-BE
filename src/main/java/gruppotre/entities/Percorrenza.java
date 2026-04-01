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

    // --------------------- COSTRUTTORI ------------------
    public Percorrenza() {

    }

    public Percorrenza(Mezzo mezzo, Tratta tratta, LocalDateTime dataPartenza) {
        this.mezzo = mezzo;
        this.tratta = tratta;
        this.dataPartenza = dataPartenza;
        this.tempoEffettivo = calcoloTempo();
    }

    // CALCOLO TEMPO IN BASE AL TRASPORTO
    private int calcoloTempo() {
        int tempo = tratta.getTempoBase();

        switch (mezzo.getTipo()) {
            case BUS -> tempo += 0; // temp_previsto + 0 tempo non cambia in bus
            case TRAM -> tempo -= 10; // temp_previsto - 10 tempo minore , tram + veloce
        }

        return tempo;
    }

// ---------------- GETTER/SETTER ------------------

    public UUID getId() {
        return id;
    }

    public int getTempoEffettivo() {
        return tempoEffettivo;
    }
    public void setTempoEffettivo(int tempoEffettivo) {
        this.tempoEffettivo= tempoEffettivo;
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

    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
        this.tempoEffettivo = calcoloTempo();
    }

    public void setTratta(Tratta tratta) {
        this.tratta = tratta;
        this.tempoEffettivo = calcoloTempo();
    }

}
