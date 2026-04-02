package gruppotre;

import gruppotre.entities.*;
import gruppotre.enums.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

public class PopulateDB {

    public static void popolaSeVuoto(EntityManager em) {
        long count = (long) em.createQuery("SELECT COUNT(m) FROM Mezzo m").getSingleResult();

        if (count == 0) {
            System.out.println("Database vuoto. Avvio Data Seeding...");
            em.getTransaction().begin();

            Distributore d1 = new Distributore("Distributore Stazione Centrale", true);
            Rivenditore r1 = new Rivenditore("Tabacchi Piazza Duomo", "Piazza Duomo 1");
            em.persist(d1);
            em.persist(r1);

            Mezzo bus = new Mezzo(gruppotre.enums.TipoVeicolo.BUS, 50);
            Mezzo tram = new Mezzo(gruppotre.enums.TipoVeicolo.TRAM, 120);
            em.persist(bus);
            em.persist(tram);

            /* NUOVA RIGA: Creazione di una Tratta di test per il menu Admin */
            Tratta tratta1 = new Tratta("Milano Centrale", "Duomo", 45);
            em.persist(tratta1);

            Utente mario = new Utente("Mario", "Rossi");
            Tessera tesseraMario = new Tessera(mario);
            mario.setTessera(tesseraMario);
            em.persist(mario);

            Biglietto b1 = new Biglietto(java.time.LocalDateTime.now().minusDays(1), d1);
            Abbonamento a1 = new Abbonamento(java.time.LocalDateTime.now().minusDays(2), r1, gruppotre.enums.TipoAbbonamento.MENSILE, tesseraMario);
            em.persist(b1);
            em.persist(a1);

            em.getTransaction().commit();

            gruppotre.dao.StatoMezzoDAO statoDAO = new gruppotre.dao.StatoMezzoDAO(em);
            statoDAO.inizializzaStato(bus);
            statoDAO.inizializzaStato(tram);

            System.out.println("Data Seeding completato.");
            System.out.println("Copia questo UUID per la Tratta di test: " + tratta1.getId());
        }
    }
}