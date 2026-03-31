package gruppotre;

import gruppotre.dao.MezzoDAO;
import gruppotre.entities.Mezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.UUID;

import static gruppotre.enums.TipoVeicolo.BUS;
import static gruppotre.enums.TipoVeicolo.TRAM;

public class Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

        MezzoDAO Mdao = new MezzoDAO(em);

       // em.getTransaction().begin();

        //Creo Mezzo

        Mezzo bus1 = new Mezzo(BUS, 50);
        Mezzo bus2 = new Mezzo(BUS, 100);
        Mezzo tram1 = new Mezzo(TRAM, 150);
        Mezzo tram2 = new Mezzo(TRAM, 200);
//
//        Mdao.save(bus1);
//        Mdao.save(bus2);
//        Mdao.save(tram1);
//       Mdao.save(tram2);

      //  em.getTransaction().commit();

//        String idbus2inDB = "a3028812-c93d-47b6-b677-5a9ff7b31d3b";
//        Mezzo trovato = Mdao.findById(idbus2inDB);
//
//        if (trovato != null) {
//            System.out.println("\nMezzo trovato: " + trovato.getTipo() +
//                    " - capienza: " + trovato.getCapienza());
//        } else {
//            System.out.println("Mezzo con ID " + idbuus2inDB + " non trovato.");
//        }

        // in Mdao.findAll() --> dentro findAll() Mettere o BUS o TRAM in base a quello che si vuole cercare
        List<Mezzo> MezziList = Mdao.findAll(TRAM);
        MezziList.forEach(m -> System.out.println("\nMezzo numero: " + m.getId() + ", Capienza: " + m.getCapienza()+ "."));

        em.close();
        emf.close();
    }
}
