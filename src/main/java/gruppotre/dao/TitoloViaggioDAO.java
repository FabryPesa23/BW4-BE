package gruppotre.dao;

import gruppotre.entities.Abbonamento;
import gruppotre.entities.Biglietto;
import gruppotre.entities.TitoloViaggio;
import gruppotre.entities.Mezzo;
import gruppotre.enums.TipoAbbonamento;
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

    public boolean checkAbbonamentoValido(Long idTessera) {
        try {
            Abbonamento a = em.createQuery(
                            "SELECT a FROM Abbonamento a WHERE a.tessera.id = :idTessera ORDER BY a.dataEmissione DESC",
                            Abbonamento.class)
                    .setParameter("idTessera", idTessera)
                    .setMaxResults(1)
                    .getSingleResult();

            LocalDateTime scadenza = a.getTipo() == TipoAbbonamento.SETTIMANALE
                    ? a.getDataEmissione().plusDays(7)
                    : a.getDataEmissione().plusDays(30);

            return LocalDateTime.now().isBefore(scadenza);
        } catch (Exception e) {
            return false; // se non trova abbonamenti o c'è un errore
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
<<<<<<< Updated upstream
=======

    // ricerca per id
    public TitoloViaggio findById(Long id) {
        return em.find(TitoloViaggio.class, id);
    }

    // conteggio di titoli per periodo
    public Long countTitoliTotaliPerPeriodo(LocalDateTime inizio, LocalDateTime fine) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.dataEmissione BETWEEN :inizio AND :fine",
                        Long.class)
                .setParameter("inizio", inizio)
                .setParameter("fine", fine)
                .getSingleResult();
    }

    // conteggio di titoli per punto d'emissione
    public Long countTitoliPerPuntoEmissione(Long idPunto) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM TitoloViaggio t WHERE t.puntoEmissione.id = :idPunto",
                        Long.class)
                .setParameter("idPunto", idPunto)
                .getSingleResult();
    }

    // conteggi combinati
//    public Long countTitoliPerPuntoEPeriodo(Long idPunto, LocalDateTime inizio, LocalDateTime fine) {
//        return em.createQuery(
//                        "SELECT COUNT(t) FROM TitoloViaggio t "
//                              "WHERE t.puntoEmissione.id = :idPunto " +
//                                "AND t.dataEmissione BETWEEN :inizio AND :fine",
//                        Long.class)
//                .setParameter("idPunto", idPunto)
//                .setParameter("inizio", inizio)
//                .setParameter("fine", fine)
//                .getSingleResult();


>>>>>>> Stashed changes
}