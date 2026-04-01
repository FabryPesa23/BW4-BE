package gruppotre;

import gruppotre.dao.MezzoDAO;
import gruppotre.dao.StatoMezzoDAO;
import gruppotre.dao.TrattaDAO;
import gruppotre.entities.Mezzo;
import gruppotre.entities.StatoMezzo;
import gruppotre.entities.Tratta;
import gruppotre.enums.StatoVeicolo;
import gruppotre.enums.TipoVeicolo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static gruppotre.enums.TipoVeicolo.BUS;
import static gruppotre.enums.TipoVeicolo.TRAM;

public class Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

        MezzoDAO mezzoDAO = new MezzoDAO(em);
        StatoMezzoDAO statoManutenzioneDAO = new StatoMezzoDAO(em);

        em.close();
        emf.close();
    }
}
