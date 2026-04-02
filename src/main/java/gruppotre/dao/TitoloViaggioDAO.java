package gruppotre.dao;

import gruppotre.entities.Biglietto;
import gruppotre.entities.TitoloViaggio;
import gruppotre.entities.Mezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;

public class TitoloViaggioDAO {
    private final EntityManager em;

    public TitoloViaggioDAO(EntityManager em) {
        this.em = em;
    }

    // salvataggio biglietto/abbonamento
    public void save(TitoloViaggio titolo) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(titolo);
            transaction.commit();
            System.out.println("Titolo emesso - ID: " + titolo.getId());
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            System.err.println("Errore emissione: " + e.getMessage());
        }
    }

    // vidimazione
    public void vidimaBiglietto(Long idBiglietto, Mezzo mezzo) {
        Biglietto b = em.find(Biglietto.class, idBiglietto);

        if (b == null) {
            System.out.println("Biglietto non trovato.");
            return;
        }

        if (b.getDataVidimazione() != null) {
            System.out.println("Biglietto già utilizzato il: " + b.getDataVidimazione());
        } else {
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                b.setDataVidimazione(LocalDateTime.now());
                b.setMezzo(mezzo);
                transaction.commit();
                System.out.println("Biglietto vidimato sul mezzo: " + mezzo.getId());
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                System.err.println("Errore vidimazione: " + e.getMessage());
            }
        }
    }
}