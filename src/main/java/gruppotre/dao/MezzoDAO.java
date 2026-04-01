package gruppotre.dao;

import gruppotre.entities.Mezzo;
import gruppotre.enums.TipoVeicolo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MezzoDAO {
    private final EntityManager em;
    public MezzoDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Mezzo mezzo) {

        em.persist(mezzo);
    }

    public Mezzo findById(String idString) {
        try {
            UUID id = UUID.fromString(idString);  // converte la stringa in UUID
            return em.find(Mezzo.class, id);
        } catch (IllegalArgumentException e) {
            System.out.println("ID non valido: " + idString);
            return null;
        }
    }

    public List<Mezzo> findAll(TipoVeicolo tipo) {
        try {
            return em.createQuery("SELECT m FROM Mezzo m WHERE m.tipo = :tipo", Mezzo.class)
                    .setParameter("tipo", tipo)
                    .getResultList();
        } catch (IllegalArgumentException | PersistenceException e) {
            System.out.println("Errore nella ricerca dei mezzi: " + e.getMessage());
            return Collections.emptyList(); // evita crash
        }
    }

}
