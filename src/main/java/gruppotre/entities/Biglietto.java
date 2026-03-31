package gruppotre.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "biglietti")
@PrimaryKeyJoinColumn(name = "id_titolo_viaggio")
public class Biglietto extends TitoloViaggio {

    private LocalDateTime dataVidimazione;

    @ManyToOne
    @JoinColumn(name = "id_mezzo")
    private Mezzo mezzo;

    // costruttore
    public Biglietto() { super(); }

    public Biglietto(LocalDateTime dataEmissione, PuntoEmissione puntoEmissione) {
        super(dataEmissione, puntoEmissione);
    }

    // get/set
    public LocalDateTime getDataVidimazione() { return dataVidimazione; }
    public void setDataVidimazione(LocalDateTime dataVidimazione) { this.dataVidimazione = dataVidimazione; }
    public Mezzo getMezzo() { return mezzo; }
    public void setMezzo(Mezzo mezzo) { this.mezzo = mezzo; }
}