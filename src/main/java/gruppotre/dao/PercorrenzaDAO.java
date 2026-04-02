package gruppotre.dao;

import gruppotre.entities.Percorrenza;
import jakarta.persistence.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PercorrenzaDAO {
    private final EntityManager em;

    public PercorrenzaDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Percorrenza p) {
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive())em.getTransaction().rollback();
            System.out.println("Errore salvataggio percorrenza: " + ex.getMessage());
        }
    }

    // CERCA PERCORRENZA TRAMITE ID
    public Percorrenza findById(String idString) {
        try {
            return em.find(Percorrenza.class, java.util.UUID.fromString(idString));
        } catch (IllegalArgumentException e) {
            System.out.println("ID non valido: " + idString);
            return null;
        }
    }

    // CERCA PERCORRENZA TRAMITE MEZZO ID
    public List<Percorrenza> findByMezzoId(String mezzoId) {
        try {
            UUID id = UUID.fromString(mezzoId);

            return em.createQuery(
                            "SELECT p FROM Percorrenza p WHERE p.mezzo.id = :id",
                            Percorrenza.class)
                    .setParameter("id", id)
                    .getResultList();

        } catch (IllegalArgumentException e) {
            System.out.println("ID non valido: " + mezzoId);
            return Collections.emptyList();
        }
    }


    // MOSTRA TUTTE LE PERCORRENZE
    public List<Percorrenza> findAll() {
        try {
            return em.createQuery("SELECT p FROM Percorrenza p", Percorrenza.class)
                    .getResultList();
        } catch (Exception ex) {
            System.out.println("Errore findAll percorrenze");
            return Collections.emptyList();
        }
    }
}
