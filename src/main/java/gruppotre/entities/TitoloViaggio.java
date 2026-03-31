package gruppotre.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "titoli_viaggio")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TitoloViaggio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataEmissione;

    @ManyToOne
    @JoinColumn(name = "id_punto_emissione")
    private PuntoEmissione puntoEmissione;

    // costruttore
    public TitoloViaggio() {}

    public TitoloViaggio(LocalDateTime dataEmissione, PuntoEmissione puntoEmissione) {
        this.dataEmissione = dataEmissione;
        this.puntoEmissione = puntoEmissione;
    }
    // get/set
    public Long getId() { return id; }
    public LocalDateTime getDataEmissione() { return dataEmissione; }
    public void setDataEmissione(LocalDateTime dataEmissione) { this.dataEmissione = dataEmissione; }
    public PuntoEmissione getPuntoEmissione() { return puntoEmissione; }
    public void setPuntoEmissione(PuntoEmissione puntoEmissione) { this.puntoEmissione = puntoEmissione; }
}
