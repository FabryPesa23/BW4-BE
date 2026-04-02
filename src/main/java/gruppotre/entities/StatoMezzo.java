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

 /*String tramid = "f4cc0e43-7c82-49aa-9fdd-4c6ae27514cc";
Mezzo tram = mezzoDAO.findById(tramid);
statoManutenzioneDAO.inizializzaStato(tram);*/
//
//        String tramId2 = "73fc3392-8396-4d12-9728-9cc1b1655b91";
//        Mezzo mezzo2 = mezzoDAO.findById(tramId2);
//           statoManutenzioneDAO.inizializzaStato(mezzo2);
//
        String busId = "ea9289c7-8468-421c-ae19-39678e66f7fe";
        Mezzo bus1 = mezzoDAO.findById(busId);
       /* statoManutenzioneDAO.inizializzaStato(bus1);*/
//
//        String busId2 = "c31211e6-b3f4-4f43-8ae7-d21d7e13f433";
//        Mezzo bus2 = mezzoDAO.findById(busId2);
//           statoManutenzioneDAO.inizializzaStato(bus2);

        // CONTROLLO STATO ATTUALE

//        String mezzoId = "a304b75a-7096-4a80-8d71-4ce4659cfe8a";
//        Mezzo mezzo = mezzoDAO.findById(mezzoId);
//
//        if (mezzo == null) {
//            System.out.println("\nMezzo non trovato");
//            return;
//        }
//        StatoMezzo statoAttuale = statoManutenzioneDAO.getStatoAttuale(mezzo);
//
//        if (statoAttuale == null) {
//            System.out.println("\nStato non trovato. Mezzo probabilmente senza stato attuale.");
//        } else {
//            System.out.println("\nLo stato attuale del mezzo è: " + statoAttuale.getStato());
//        }

        // CAMBIO STATO (METTERE PREVISTAFINE SE MEZZO VA IN_MANUTENZIONE
        // SE MEZZO RITORNA IN_SERVIZIO SI METTE NULL)
//
//        Mezzo bus1 = mezzoDAO.findById("c31211e6-b3f4-4f43-8ae7-d21d7e13f433");
        //LocalDate previstaFine = LocalDate.now().plusDays(7);
        /*statoManutenzioneDAO.cambiaStato(bus1, StatoVeicolo.RITARDO, null);*/

        // CONTROLLA STATO ATTUALE DOPO MANUTENZINE
//        StatoMezzo manutenzione = statoManutenzioneDAO.getStatoAttuale(mezzo);
//        System.out.println("Stato dopo cambio: " + manutenzione.getStato() +
//                ", dataInizio: " + manutenzione.getDataInizio() +
//                ", dataPrevistaFine: " + manutenzione.getDataPrevistaFine() +
//                ", dataFine: " + manutenzione.getDataFine());

        // VERIFICA MEZZI IN MANUTENZIONE  MEZZI IN SERVIZIO E IN RITARDO
        List<Mezzo> inServizio = statoManutenzioneDAO.findMezziInServizio();
        List<Mezzo> inManutenzione = statoManutenzioneDAO.findMezziInManutenzione();
        List<Mezzo> inRitardo = statoManutenzioneDAO.findMezziInRitardo();

        System.out.println("\nMezzi in servizio: ");
        inServizio.forEach(m -> System.out.println("- " + m.getId() + " (" + m.getTipo() + ")"));

        System.out.println("\nMezzi in manutenzione:");
        inManutenzione.forEach(m -> System.out.println("-UUID " + m.getId() + " (" + m.getTipo() + ")"));

        System.out.println("\nMezzi in ritardo:");
        inRitardo.forEach(m -> System.out.println("-UUID " + m.getId() + " (" + m.getTipo() + ")"));

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
