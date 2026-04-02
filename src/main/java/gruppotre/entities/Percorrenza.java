package gruppotre.entities;

import gruppotre.dao.MezzoDAO;
import gruppotre.dao.PercorrenzaDAO;
import gruppotre.dao.TrattaDAO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "percorrenza")
public class Percorrenza {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;

    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

    @Column(name = "data_partenza", nullable = false)
    private LocalDateTime dataPartenza;

    @Column(name = "tempo_effettivo")
    private int tempoEffettivo; // minuti

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

        MezzoDAO Mdao = new MezzoDAO(em);
        TrattaDAO trattaDAO = new TrattaDAO(em);

        // METODO PER AGGIUNGERE NUOVA PERCORRENZA CON TRATTA E MEZZO
        // CALCOLA ANCHE IL TEMPO-EFFETTIVO IN BASE AL MEZZO
        Mezzo mezzo = Mdao.findById("a304b75a-7096-4a80-8d71-4ce4659cfe8a");
        Tratta tratta = trattaDAO.findById("763f91f1-3d48-408d-bf81-1e268a905432");

        if (mezzo == null ) {
            System.out.println("Mezzo non trovato");
            return;
        }
        if (tratta == null ) {
            System.out.println("Tratta non trovato");
            return;
        }

        Percorrenza p1 = new Percorrenza(mezzo, tratta, LocalDateTime.now());

        em.getTransaction().begin();
//        em.persist(p1);
        em.getTransaction().commit();

        // METODO PER VEDERE TUTTE LE PERCORRENZE
        PercorrenzaDAO pDao = new PercorrenzaDAO(em);
        List<Percorrenza> percorrenze = pDao.findAll();

        // SERVE PER 'FORMATTARE LA DATA' COSI ESCE MEGLIO QUANDO SOUT IN
        // CONSOLE PRINTA LA DATA E NON ESCE ES: 2026-04-01T12:01:37.736699
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Percorrenza p : percorrenze) {
            System.out.println("Mezzo: " + p.getMezzo().getTipo());
            System.out.println("Tratta: " + p.getTratta().getZonaPartenza() + " -> " + p.getTratta().getCapolinea());
            System.out.println("Data partenza: " + p.getDataPartenza().format(dtf));
            System.out.println("Tempo effettivo: " + p.getTempoEffettivo());
            System.out.println("------");
        }
    }

    // --------------------- COSTRUTTORI ------------------
    public Percorrenza() {

    }

    public Percorrenza(Mezzo mezzo, Tratta tratta, LocalDateTime dataPartenza) {
        this.mezzo = mezzo;
        this.tratta = tratta;
        this.dataPartenza = dataPartenza;
        this.tempoEffettivo = calcoloTempo();
    }

    // CALCOLO TEMPO IN BASE AL TRASPORTO
    private int calcoloTempo() {
        int tempo = tratta.getTempoBase();

        switch (mezzo.getTipo()) {
            case BUS -> tempo += 0; // temp_previsto + 0 tempo non cambia in bus
            case TRAM -> tempo -= 10; // temp_previsto - 10 tempo minore , tram + veloce
        }

        return tempo;
    }

// ---------------- GETTER/SETTER ------------------

    public UUID getId() {
        return id;
    }

    public int getTempoEffettivo() {
        return tempoEffettivo;
    }
    public void setTempoEffettivo(int tempoEffettivo) {
        this.tempoEffettivo= tempoEffettivo;
    }

    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public Tratta getTratta() {
        return tratta;
    }

    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
        this.tempoEffettivo = calcoloTempo();
    }

    public void setTratta(Tratta tratta) {
        this.tratta = tratta;
        this.tempoEffettivo = calcoloTempo();
    }

    @Override
    public String toString() {
        return "Percorrenza{" +
                "id=" + id +
                ", mezzo=" + mezzo +
                ", tratta=" + tratta +
                ", dataPartenza=" + dataPartenza +
                ", tempoEffettivo=" + tempoEffettivo +
                '}';
    }
}
