package gruppotre.dao;

import gruppotre.entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UtenteDAO {
    private final EntityManager em;

    public UtenteDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Utente utente) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(utente);
            et.commit();
            System.out.println("Salvataggio riuscito: Utente e Tessera inseriti.");
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        }
    }
}