package gruppotre.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tessere")
public class Tessera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emissione")
    private LocalDate dataEmissione;

    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;

    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Tessera() {}

    public Tessera(Utente utente) {
        this.utente = utente;
        this.dataEmissione = LocalDate.now();
        this.dataScadenza = this.dataEmissione.plusYears(1);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}