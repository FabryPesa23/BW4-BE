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

    @Column(name = "temp_base")
    private int tempBase;  // tempo "standard" della tratta senza mezzo

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veicolo", nullable = false)
    private TipoVeicolo tipo; // mezzo standard per questa tratta

    @OneToMany(mappedBy = "tratta")
    private List<Percorrenza> percorrenze;

    // ------------------- COSTRUTTORI -------------------
    public Tratta() { }

    public Tratta(String zonaPartenza, String capolinea, int tempBase, TipoVeicolo tipo) {
        this.zonaPartenza = zonaPartenza;
        this.capolinea = capolinea;
        this.tempBase = tempBase;
        this.tipo = tipo;
    }

    // ------------------- GETTER/SETTER -------------------
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getZonaPartenza() { return zonaPartenza; }
    public void setZonaPartenza(String zonaPartenza) { this.zonaPartenza = zonaPartenza; }

    public String getCapolinea() { return capolinea; }
    public void setCapolinea(String capolinea) { this.capolinea = capolinea; }

    public int getTempBase() { return tempBase; }
    public void setTempBase(int tempBase) { this.tempBase = tempBase; }

    public TipoVeicolo getTipo() { return tipo; }
    public void setTipo(TipoVeicolo tipo) { this.tipo = tipo; }

    public List<Percorrenza> getPercorrenze() { return percorrenze; }
    public void setPercorrenze(List<Percorrenza> percorrenze) { this.percorrenze = percorrenze; }

    // Calcola il tempo previsto combinando tratta + tipo mezzo
    public int calcolaTempoPrevisto(Mezzo mezzo) {
        int tempo = this.tempBase;
        switch (mezzo.getTipo()) {
            case BUS -> tempo += 0;
            case TRAM -> tempo -= 10;
            default -> tempo = tempBase;
        }
        return tempo;
    }
}
