package gruppotre.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "distributori")
@PrimaryKeyJoinColumn(name = "id_punto_emissione")
public class Distributore extends PuntoEmissione {

    private boolean attivo;

    public Distributore() { super(); }

    public Distributore(String nome, boolean attivo) {
        super(nome);
        this.attivo = attivo;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }
}