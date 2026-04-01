package gruppotre.entities;

import gruppotre.enums.TipoAbbonamento;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "abbonamenti")
@PrimaryKeyJoinColumn(name = "id_titolo_viaggio")
public class Abbonamento extends TitoloViaggio {

    @Enumerated(EnumType.STRING)
    private TipoAbbonamento tipo;

    @ManyToOne
    @JoinColumn(name = "id_tessera")
    private Tessera tessera;

    // costruttore
    public Abbonamento() { super(); }

    public Abbonamento(LocalDateTime dataEmissione, PuntoEmissione puntoEmissione, TipoAbbonamento tipo, Tessera tessera) {
        super(dataEmissione, puntoEmissione);
        this.tipo = tipo;
        this.tessera = tessera;
    }

    // get/set
    public TipoAbbonamento getTipo() { return tipo; }
    public void setTipo(TipoAbbonamento tipo) { this.tipo = tipo; }
    public Tessera getTessera() { return tessera; }
    public void setTessera(Tessera tessera) { this.tessera = tessera; }
}

