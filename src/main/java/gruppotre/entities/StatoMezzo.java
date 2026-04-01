package gruppotre.entities;

import gruppotre.dao.MezzoDAO;
import gruppotre.dao.StatoMezzoDAO;
import gruppotre.enums.StatoVeicolo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static gruppotre.enums.TipoVeicolo.BUS;

@Entity
@Table(name = "stato_mezzo")
public class StatoMezzo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Mezzo mezzo;

    @Enumerated(EnumType.STRING)
    private StatoVeicolo stato;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(name = "data_prevista_fine")
    private LocalDate dataPrevistaFine;

    public StatoMezzo() {}

    public StatoMezzo(Mezzo mezzo, StatoVeicolo stato, LocalDate dataInizio, LocalDate dataPrevistaFine) {
        this.mezzo = mezzo;
        this.stato = stato;
        this.dataInizio = dataInizio;
        this.dataFine = null;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

        MezzoDAO mezzoDAO = new MezzoDAO(em);
        StatoMezzoDAO statoManutenzioneDAO = new StatoMezzoDAO(em);

        // CREA MEZZO
        Mezzo bus = new Mezzo(BUS, 50);
        // mezzoDAO.save(bus);

        // STATO INIZIALE
        //   statoManutenzioneDAO.inizializzaStato(bus);

        // CONTROLLO STATO ATTUALE
        String mezzoId = "a304b75a-7096-4a80-8d71-4ce4659cfe8a";
        Mezzo mezzo = mezzoDAO.findById(mezzoId);

        if (mezzo == null) {
            System.out.println("\nMezzo non trovato");
            return;
        }
        StatoMezzo statoAttuale = statoManutenzioneDAO.getStatoAttuale(mezzo);

        if (statoAttuale == null) {
            System.out.println("\nStato non trovato. Mezzo probabilmente senza stato attuale.");
        } else {
            System.out.println("\nLo stato attuale del mezzo è: " + statoAttuale.getStato());
        }

        // CAMBIO STATO
//        LocalDate previstaFine = LocalDate.now().plusDays(7);
//        statoManutenzioneDAO.cambiaStato(bus, StatoVeicolo.IN_MANUTENZIONE, previstaFine);

        // CONTROLLA STATO ATTUALE DOPO MANUTENZINE
        StatoMezzo manutenzione = statoManutenzioneDAO.getStatoAttuale(mezzo);
        System.out.println("Stato dopo cambio: " + manutenzione.getStato() +
                ", dataInizio: " + manutenzione.getDataInizio() +
                ", dataPrevistaFine: " + manutenzione.getDataPrevistaFine() +
                ", dataFine: " + manutenzione.getDataFine());

        // VERIFICA MEZZI IN MANUTENZIONE E MEZZI IN SERVIZIO
        List<Mezzo> inServizio = statoManutenzioneDAO.findMezziInServizio();
        List<Mezzo> inManutenzione = statoManutenzioneDAO.findMezziInManutenzione();

        System.out.println("\nMezzi in servizio: ");
        inServizio.forEach(m -> System.out.println("- " + m.getId() + " (" + m.getTipo() + ")"));

        System.out.println("\nMezzi in manutenzione:");
        inManutenzione.forEach(m -> System.out.println("-UUID " + m.getId() + " (" + m.getTipo() + ")"));

        em.close();
        emf.close();
    }

    // GETTER / SETTER
    public UUID getId() { return id; }

    public Mezzo getMezzo() { return mezzo; }
    public void setMezzo(Mezzo mezzo) { this.mezzo = mezzo; }

    public StatoVeicolo getStato() { return stato; }
    public void setStato(StatoVeicolo stato) { this.stato = stato; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataPrevistaFine() { return dataPrevistaFine; }
    public void setDataPrevistaFine(LocalDate dataPrevistaFine) { this.dataPrevistaFine = dataPrevistaFine; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }
}
