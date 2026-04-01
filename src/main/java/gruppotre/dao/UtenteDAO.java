package gruppotre.dao;

import gruppotre.entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

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

    public Utente findById(Long id) {
        return em.find(Utente.class, id);
    }

    public List<Utente> findAll() {
        return em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
    }

    public Utente findByNumeroTessera(Long numeroTessera) {
        TypedQuery<Utente> query = em.createQuery(
                "SELECT u FROM Utente u WHERE u.tessera.id = :num", Utente.class);
        query.setParameter("num", numeroTessera);
        return query.getSingleResult();
    }

    public boolean isTesseraValida(Long numeroTessera) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Tessera t WHERE t.id = :num AND t.dataScadenza > :oggi", Long.class);
        query.setParameter("num", numeroTessera);
        query.setParameter("oggi", LocalDate.now());
        return query.getSingleResult() > 0;
    }

    public void rinnovaTessera(Long numeroTessera) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Utente u = findByNumeroTessera(numeroTessera);
            if (u != null && u.getTessera() != null) {
                u.getTessera().setDataEmissione(LocalDate.now());
                u.getTessera().setDataScadenza(LocalDate.now().plusYears(1));
                em.merge(u);
                System.out.println("Tessera rinnovata per un altro anno.");
            }
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) et.rollback();
            e.printStackTrace();
        }
    }
}