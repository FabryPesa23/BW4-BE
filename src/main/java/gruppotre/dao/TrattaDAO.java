package gruppotre.dao;

import gruppotre.entities.Tratta;
import gruppotre.enums.TipoVeicolo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TrattaDAO {

    private final EntityManager em;

    public TrattaDAO(EntityManager em) {
        this.em = em;
    }

    public Tratta findById(UUID id) {
        try {
            return em.find(Tratta.class, id);
        } catch (PersistenceException e) {
            System.out.println("Errore di persistenza: " + e.getMessage());
            return null;
        }
    }

    public List<Tratta> findAll() {
        try {
            return (List<Tratta>) em.createQuery("SELECT t FROM Tratta t", Tratta.class).getResultList();
        } catch (IllegalArgumentException e) {
            System.out.println("Errore nella ricerca della tratta: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Tratta> findByTipo(TipoVeicolo tipo) {
        try {
            return em.createQuery("SELECT t FROM Tratta t WHERE t.tipo = :tipo", Tratta.class)
                    .setParameter("tipo", tipo)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore nella ricerca per tipo: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean update(Tratta tratta) {
        try {
            em.getTransaction().begin();
            em.merge(tratta);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Errore nell'aggiornamento: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(Tratta tratta) {
        try {
            em.getTransaction().begin();
            em.remove(em.contains(tratta) ? tratta : em.merge(tratta));
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Errore nella cancellazione: " + e.getMessage());
            return false;
        }
    }

    public List<Tratta> findByZonaPartenza(String zona) {
        try {
            return em.createQuery("SELECT t FROM Tratta t WHERE t.zonaPartenza = :zona", Tratta.class)
                    .setParameter("zona", zona)
                    .getResultList();
        } catch (PersistenceException e) {
            System.out.println("Errore nella ricerca per zona: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
