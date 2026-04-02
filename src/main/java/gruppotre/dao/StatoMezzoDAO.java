package gruppotre.dao;

import gruppotre.entities.Mezzo;
import gruppotre.entities.StatoMezzo;
import gruppotre.enums.StatoVeicolo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class StatoMezzoDAO {

    private final EntityManager em;
    public   StatoMezzoDAO(EntityManager em) {
        this.em = em;
    }

    // STATO ATTUALE
    public StatoMezzo getStatoAttuale(Mezzo mezzo) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatoMezzo s WHERE s.mezzo = :mezzo AND s.dataFine IS NULL",
                            StatoMezzo.class)
                    .setParameter("mezzo", mezzo)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Nessuno stato attuale trovato");
            return null;
        }
    }

    // SETTARE STATO DEFAULT A IN_SERVIZIO
    public void inizializzaStato(Mezzo mezzo) {
        if (getStatoAttuale(mezzo) == null) {
           try {
               em.getTransaction().begin();
               StatoMezzo stato = new StatoMezzo(mezzo, StatoVeicolo.IN_SERVIZIO, LocalDate.now(), null);
               em.persist(stato);
               em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Errore inizializzazione stato: " + e.getMessage());
        }
        }

    }
    // CAMBIA STATO
    public void cambiaStato(Mezzo mezzo, StatoVeicolo nuovoStato, LocalDate dataPrevistaFine) {
        try {
            em.getTransaction().begin();

            StatoMezzo attuale = getStatoAttuale(mezzo);
            if (attuale != null) {
                attuale.setDataFine(LocalDate.now());
                em.merge(attuale);
            }

            StatoMezzo nuovo = new StatoMezzo();
            nuovo.setMezzo(mezzo);
            nuovo.setStato(nuovoStato);
            nuovo.setDataInizio(LocalDate.now());
            nuovo.setDataPrevistaFine(dataPrevistaFine);
            nuovo.setDataFine(null);

            em.persist(nuovo);

            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Errore cambio stato: " + e.getMessage());
        }
    }

    // TUTTI GLI STATI DI UN MEZZO
    public List<StatoMezzo> findByMezzo(Mezzo mezzo) {
        try {
            return em.createQuery(
                            "SELECT s FROM StatoMezzo s WHERE s.mezzo = :mezzo",
                            StatoMezzo.class)
                    .setParameter("mezzo", mezzo)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore ricerca stati: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // MEZZI ATTUALI IN MANUTENZIONE
    public List<Mezzo> findMezziInManutenzione() {
        try {
            return em.createQuery(
                            "SELECT s.mezzo FROM StatoMezzo s WHERE s.stato = :stato AND s.dataFine IS NULL",
                            Mezzo.class)
                    .setParameter("stato", StatoVeicolo.IN_MANUTENZIONE)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore query manutenzione: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // MEZZI ATTUALMENTE IN SERVIZIO
    public List<Mezzo> findMezziInServizio() {
        try {
            return em.createQuery(
                            "SELECT s.mezzo FROM StatoMezzo s WHERE s.stato = :stato AND s.dataFine IS NULL",
                            Mezzo.class)
                    .setParameter("stato", StatoVeicolo.IN_SERVIZIO)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore query servizio: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // MEZZI ATTUALI IN RITARDO
    public List<Mezzo> findMezziInRitardo() {
        try {
            return em.createQuery(
                            "SELECT s.mezzo FROM StatoMezzo s WHERE s.stato = :stato AND s.dataFine IS NULL",
                            Mezzo.class)
                    .setParameter("stato", StatoVeicolo.RITARDO)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore query mezzi in ritardo: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    }

