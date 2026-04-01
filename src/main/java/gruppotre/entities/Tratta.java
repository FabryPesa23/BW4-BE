package gruppotre.entities;

import gruppotre.enums.TipoVeicolo;
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

    @Column(name = "tempo_previsto")
    private int tempoBase; // tempo "standard" della tratta senza mezzo


    @OneToMany(mappedBy = "tratta")
    private List<Percorrenza> percorrenze;

    // ------------------- COSTRUTTORI -------------------
    public Tratta() { }

    public Tratta(String zonaPartenza, String capolinea,int tempoBase) {
        this.zonaPartenza = zonaPartenza;
        this.capolinea = capolinea;
        this.tempoBase = tempoBase;
    }

    // ------------------- GETTER/SETTER -------------------

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getZonaPartenza() { return zonaPartenza; }
    public void setZonaPartenza(String zonaPartenza) { this.zonaPartenza = zonaPartenza; }

    public String getCapolinea() { return capolinea; }
    public void setCapolinea(String capolinea) { this.capolinea = capolinea; }

    public int getTempoBase() {
        return tempoBase;
    }

    public void setTempoBase(int tempoPrevisto) {
        this.tempoBase = tempoBase;
    }

    public List<Percorrenza> getPercorrenze() { return percorrenze; }
    public void setPercorrenze(List<Percorrenza> percorrenze) { this.percorrenze = percorrenze; }

}
