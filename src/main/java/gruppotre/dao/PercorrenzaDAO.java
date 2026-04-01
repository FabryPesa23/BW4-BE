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

    public Percorrenza findById(UUID id) {
        return em.find(Percorrenza.class, id);
    }

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
