package gruppotre.entities;

import gruppotre.dao.MezzoDAO;
import gruppotre.dao.TrattaDAO;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import gruppotre.enums.TipoVeicolo;

import static gruppotre.enums.TipoVeicolo.BUS;
import static gruppotre.enums.TipoVeicolo.TRAM;

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


    // --------------------- GETTER/SETTER -----------------

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

    // -------------------- COSTRUTTORI ---------------------
    public Mezzo() {

    }
    public  Mezzo(TipoVeicolo tipo, int capienza) {
        this.tipo = tipo;
        this.capienza = capienza;
    }

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

        MezzoDAO Mdao = new MezzoDAO(em);
        TrattaDAO trattaDAO = new TrattaDAO(em);

        // em.getTransaction().begin();

        // CREO MEZZI
        Mezzo bus2 = new Mezzo(BUS, 100);
        Mezzo tram1 = new Mezzo(TRAM, 150);
        Mezzo tram2 = new Mezzo(TRAM, 200);

//        Mdao.save(bus2);
//        Mdao.save(tram1);
//        Mdao.save(tram2);

        //  em.getTransaction().commit();

//        String idbus2inDB = "a3028812-c93d-47b6-b677-5a9ff7b31d3b";
//        Mezzo trovato = Mdao.findById(idbus2inDB);
//
//        if (trovato != null) {
//            System.out.println("\nMezzo trovato: " + trovato.getTipo() +
//                    " - capienza: " + trovato.getCapienza());
//        } else {
//            System.out.println("Mezzo con ID " + idbuus2inDB + " non trovato.");
//        }

        // in Mdao.findAll() --> dentro findAll() Mettere o BUS o TRAM in base a quello che si vuole cercare
//        List<Mezzo> MezziList = Mdao.findAll(TRAM);
//        MezziList.forEach(m -> System.out.println("\nMezzo numero: " + m.getId() + ", Capienza: " + m.getCapienza()+ "."));


        Tratta tratta1 = new Tratta("Roma", "Bologna", 50); // tempoPrevisto = 50
        Tratta tratta2 = new Tratta("Milano", "Torino", 40); // tempoPrevisto = 30
        Tratta tratta3 = new Tratta("Milano", "Torino", 40); // tempoPrevisto = 40

//        trattaDAO.update(tratta1);
//        trattaDAO.update(tratta2);
//          trattaDAO.update(tratta3);
//
//        System.out.println("\nTratta1 Tempo Previsto nel DB: " + tratta1.getTempoPrevisto() + " min");
//        System.out.println("\nTratta2 Tempo Previsto nel DB: " + tratta2.getTempoPrevisto() + " min");

        List<Tratta> tratteDaRoma = trattaDAO.findByZonaPartenza("Roma");
        System.out.println("\nTratte in partenza da Roma: \n");
        for (Tratta t : tratteDaRoma) {
            System.out.println("- " + t.getZonaPartenza() + " -> " + t.getCapolinea() + ", tempo previsto per " + " : " + t.getTempoBase() + " min");
        }

        List<Tratta> tratteDaMilano = trattaDAO.findByZonaPartenza("Milano");
        System.out.println("\nTratte in partenza da Milano: \n");
        for (Tratta t : tratteDaMilano) {
            System.out.println("- " + t.getZonaPartenza() + " -> " + t.getCapolinea() + ", tempo previsto per " +  " : " + t.getTempoBase() + " min");
        }

        em.close();
        emf.close();
    }

}
