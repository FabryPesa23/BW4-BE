package gruppotre.dao;

import gruppotre.entities.PuntoEmissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class PuntoEmissioneDAO {
    private final EntityManager em;

    public PuntoEmissioneDAO(EntityManager em) {
        this.em = em;
    }

    public void save(PuntoEmissione punto) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(punto);
            et.commit();
            System.out.println("Punto Emissione salvato correttamente.");
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        }
    }
}