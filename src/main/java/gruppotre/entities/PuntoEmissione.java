package gruppotre.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "punti_emissione")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PuntoEmissione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    public PuntoEmissione() {}

    public PuntoEmissione(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}