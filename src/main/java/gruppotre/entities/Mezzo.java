package gruppotre.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import gruppotre.enums.TipoVeicolo;

@Entity
@Table(name = "mezzo")
public class Mezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TipoVeicolo tipo;

    @Column(name = "capienza", nullable = false)
    private int capienza;

    @OneToMany(mappedBy = "mezzo")
    private List<StatoMezzo> stati;

    @OneToMany(mappedBy = "mezzo")
    private List<Percorrenza> percorrenze;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoVeicolo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeicolo tipo) {
        this.tipo = tipo;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public List<StatoMezzo> getStati() {
        return stati;
    }

    public void setStati(List<StatoMezzo> stati) {
        this.stati = stati;
    }

    public List<Percorrenza> getPercorrenze() {
        return percorrenze;
    }

    public void setPercorrenza(List<Percorrenza> percorrenze) {
        this.percorrenze = percorrenze;
    }

    public Mezzo() {

    }
    public  Mezzo(TipoVeicolo tipo, int capienza) {
        this.tipo = tipo;
        this.capienza = capienza;
    }


}
