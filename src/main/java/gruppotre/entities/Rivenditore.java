package gruppotre.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "rivenditori")
@PrimaryKeyJoinColumn(name = "id_punto_emissione")
public class Rivenditore extends PuntoEmissione {

    private String indirizzo;

    public Rivenditore() { super(); }

    public Rivenditore(String nome, String indirizzo) {
        super(nome);
        this.indirizzo = indirizzo;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}