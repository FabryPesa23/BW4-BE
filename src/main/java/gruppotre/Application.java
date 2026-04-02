package gruppotre;

import gruppotre.entities.*;
import gruppotre.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class Application {

    public static void main(String[] args) {

        // ACCENSIONE DEL DATABASE
        System.out.println("Avvio del sistema... connessione al database in corso.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();
        // Innesco per popolare il DB se è vuoto
        PopulateDB.popolaSeVuoto(em);

        // SETUP DELLO SCANNER E DEL LOOP PRINCIPALE
        Scanner scanner = new Scanner(System.in);

        // Questa variabile tiene in vita il programma. Finché è 'true', il menu continua a riapparire
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("   SISTEMA GESTIONE TRASPORTI PUBBLICI    ");
        System.out.println("==========================================");

        while (running) {
            System.out.println("\n--- MENU PRINCIPALE ---");
            System.out.println("1. Area Passeggero");
            System.out.println("2. Area Amministratore");
            System.out.println("0. Esci dal sistema");
            System.out.print("Scegli un'opzione: ");

            // Leggiamo la riga scritta dall'utente
            String input = scanner.nextLine();

            // Lo switch smista il traffico in base a cosa ha digitato l'utente
            switch (input) {
                case "1":
                    // Entriamo nel sottomenu Passeggero
                    gestisciMenuUtente(scanner, em);
                    break;
                case "2":
                    // Entriamo nel sottomenu Amministratore
                    gestisciMenuAdmin(scanner, em);
                    break;
                case "0":
                    // Spegniamo il loop
                    System.out.println("Chiusura del sistema in corso... Arrivederci!");
                    running = false;
                    break;
                default:
                    // Se digita "A" o "5", il programma non crasha ma lo rimprovera
                    System.out.println("ERRORE: Inserisci 1, 2 o 0.");
            }
        }

        // CHIUSURA PULITA DELLE CONNESSIONI
        scanner.close();
        em.close();
        emf.close();
    }

    // ==========================================
    // SOTTOMENU UTENTE (CABALATO AL DATABASE)
    // ==========================================
    private static void gestisciMenuUtente(Scanner scanner, EntityManager em) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- AREA PASSEGGERO ---");
            System.out.println("1. Compra biglietto");
            System.out.println("2. Timbra biglietto");
            System.out.println("3. Richiedi nuova Tessera (e crea Utente)");
            System.out.println("4. Sottoscrivi Abbonamento");
            System.out.println("0. Torna indietro");
            System.out.print("Scelta: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // ACQUISTO BIGLIETTO
                    System.out.println("\nELENCO PUNTI VENDITA DISPONIBILI:");
                    java.util.List<PuntoEmissione> puntiVenditaUtente = em.createQuery("SELECT p FROM PuntoEmissione p", PuntoEmissione.class).getResultList();
                    for (PuntoEmissione pvu : puntiVenditaUtente) {
                        System.out.println("- ID: " + pvu.getId() + " | Tipo: " + pvu.getClass().getSimpleName());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserisci l'ID del Punto Vendita (es. 1 per un Distributore o 0 per annullare):");
                    long idPunto = leggiNumeroSicuro(scanner);
                    if (idPunto == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    // Peschiamo il punto vendita dal DB
                    PuntoEmissione punto = em.find(PuntoEmissione.class, idPunto);

                    if (punto != null) {
                        em.getTransaction().begin();
                        Biglietto nuovoBiglietto = new Biglietto(LocalDateTime.now(), punto);
                        em.persist(nuovoBiglietto);
                        em.getTransaction().commit();

                        System.out.println("[SUCCESSO] Biglietto acquistato! Il tuo ID Biglietto è: " + nuovoBiglietto.getId());
                    } else {
                        System.out.println("ERRORE: Punto vendita inesistente. Controlla il database.");
                    }
                    break;

                case "2":
                    System.out.println("\nELENCO BIGLIETTI A SISTEMA:");
                    java.util.List<Biglietto> bigliettiUtente = em.createQuery("SELECT b FROM Biglietto b", Biglietto.class).getResultList();
                    for (Biglietto bu : bigliettiUtente) {
                        System.out.println("- ID: " + bu.getId() + " | Data Emissione: " + bu.getDataEmissione());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'ID del Biglietto da vidimare (o 0 per annullare):");
                    long idBiglietto = leggiNumeroSicuro(scanner);
                    if (idBiglietto == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    /* AGGIUNTA: Stampa dell'elenco dei mezzi disponibili per facilitare l'inserimento */
                    System.out.println("\nELENCO MEZZI IN ARRIVO ALLA FERMATA:");
                    java.util.List<Mezzo> listaMezziUtente = em.createQuery("SELECT m FROM Mezzo m", Mezzo.class).getResultList();
                    for (Mezzo m : listaMezziUtente) {
                        System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Copia e inserisci l'UUID del Mezzo su cui stai salendo (o 0 per annullare):");
                    UUID idMezzo = leggiUUIDSicuro(scanner);
                    if (idMezzo == null) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Biglietto bigliettoDaTimbrare = em.find(Biglietto.class, idBiglietto);
                    Mezzo mezzo = em.find(Mezzo.class, idMezzo);

                    if (bigliettoDaTimbrare == null) {
                        System.out.println("Errore: Biglietto non trovato.");
                    } else if (mezzo == null) {
                        System.out.println("Errore: Mezzo non trovato.");
                    } else if (bigliettoDaTimbrare.getDataVidimazione() != null) {
                        System.out.println("Avviso: Il biglietto risulta già vidimato in data " + bigliettoDaTimbrare.getDataVidimazione());
                    } else {
                        em.getTransaction().begin();
                        bigliettoDaTimbrare.setDataVidimazione(java.time.LocalDateTime.now());
                        bigliettoDaTimbrare.setMezzo(mezzo);
                        em.merge(bigliettoDaTimbrare);
                        em.getTransaction().commit();

                        System.out.println("Vidimazione registrata con successo sul mezzo: " + mezzo.getTipo());
                    }
                    break;
                case "3":
                    // CREAZIONE UTENTE E TESSERA
                    System.out.print("Inserisci il tuo Nome: ");
                    String nome = scanner.nextLine().trim();
                    System.out.print("Inserisci il tuo Cognome: ");
                    String cognome = scanner.nextLine().trim();

                    em.getTransaction().begin();
                    Utente nuovoUtente = new Utente(nome, cognome);
                    Tessera nuovaTessera = new Tessera(nuovoUtente);
                    nuovoUtente.setTessera(nuovaTessera);

                    em.persist(nuovoUtente);
                    em.getTransaction().commit();

                    System.out.println("[SUCCESSO] Utente e Tessera creati! ID Tessera: " + nuovaTessera.getId() + " (Scade il: " + nuovaTessera.getDataScadenza() + ")");
                    break;

                case "4":
                    // SOTTOSCRIZIONE ABBONAMENTO
                    System.out.println("\nELENCO TESSERE A SISTEMA:");
                    java.util.List<Tessera> tessereAbbonamento = em.createQuery("SELECT t FROM Tessera t", Tessera.class).getResultList();
                    for (Tessera ta : tessereAbbonamento) {
                        System.out.println("- ID Tessera: " + ta.getId() + " | Intestatario: " + ta.getUtente().getNome() + " " + ta.getUtente().getCognome());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserisci l'ID della tua Tessera (o 0 per annullare):");
                    long idTessera = leggiNumeroSicuro(scanner);
                    if (idTessera == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Tessera tessera = em.find(Tessera.class, idTessera);

                    if (tessera == null) {
                        System.out.println("ERRORE: Tessera inesistente. Fai la tessera al punto 3 prima di abbonarti.");
                        break;
                    }

                    if (tessera.getDataScadenza().isBefore(LocalDate.now())) {
                        System.out.println("ERRORE: La tua tessera è scaduta. Non puoi abbonarti.");
                        break;
                    }

                    System.out.println("\nELENCO PUNTI VENDITA DISPONIBILI:");
                    java.util.List<PuntoEmissione> puntiVenditaAbbonamento = em.createQuery("SELECT p FROM PuntoEmissione p", PuntoEmissione.class).getResultList();
                    for (PuntoEmissione pva : puntiVenditaAbbonamento) {
                        System.out.println("- ID: " + pva.getId() + " | Tipo: " + pva.getClass().getSimpleName());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserisci l'ID del Punto Vendita (o 0 per annullare):");
                    long idPuntoV = leggiNumeroSicuro(scanner);
                    if (idPuntoV == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    PuntoEmissione puntoV = em.find(PuntoEmissione.class, idPuntoV);

                    if (puntoV == null) {
                        System.out.println("ERRORE: Punto vendita inesistente.");
                        break;
                    }

                    System.out.println("Tipo di abbonamento? Digita 1 per SETTIMANALE, 2 per MENSILE:");
                    String sceltaAbb = scanner.nextLine().trim();
                    TipoAbbonamento tipoEnum;

                    if (sceltaAbb.equals("1")) {
                        tipoEnum = TipoAbbonamento.SETTIMANALE;
                    } else if (sceltaAbb.equals("2")) {
                        tipoEnum = TipoAbbonamento.MENSILE;
                    } else {
                        System.out.println("ERRORE: Scelta non valida.");
                        break;
                    }

                    em.getTransaction().begin();
                    Abbonamento nuovoAbbonamento = new Abbonamento(LocalDateTime.now(), puntoV, tipoEnum, tessera);
                    em.persist(nuovoAbbonamento);
                    em.getTransaction().commit();

                    System.out.println("[SUCCESSO] Abbonamento " + tipoEnum + " attivato con successo! ID: " + nuovoAbbonamento.getId());
                    break;

                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("ERRORE: Scelta non valida.");
            }
        }
    }


    // ========================
    // SOTTOMENU AMMINISTRATORE
    // ========================
    /**
     * Gestisce la logica di amministrazione: estrazione dati statistici,
     * manutenzione e registrazione delle percorrenze.
     */
    private static void gestisciMenuAdmin(Scanner scanner, EntityManager em) {
        boolean inMenu = true;

        // Inizializzazione dei DAO per delegare la logica di accesso ai dati
        gruppotre.dao.StatoMezzoDAO statoDAO = new gruppotre.dao.StatoMezzoDAO(em);
        gruppotre.dao.PercorrenzaDAO percorrenzaDAO = new gruppotre.dao.PercorrenzaDAO(em);

        while (inMenu) {
            System.out.println("\n--- AREA AMMINISTRATORE ---");
            System.out.println("1. Verificare la validità di una tessera");
            System.out.println("2. Statistiche: Totale titoli emessi per Punto Vendita");
            System.out.println("3. Statistiche: Totale biglietti vidimati per Mezzo");
            System.out.println("4. Gestione stato e manutenzione mezzi");
            System.out.println("5. Registrare una nuova Percorrenza");
            System.out.println("6. Statistiche: Storico percorrenze per Mezzo");
            System.out.println("0. Tornare al menu precedente");
            System.out.print("Scelta: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("\nELENCO TESSERE A SISTEMA:");
                    java.util.List<Tessera> tessereAdmin = em.createQuery("SELECT t FROM Tessera t", Tessera.class).getResultList();
                    for (Tessera ta : tessereAdmin) {
                        System.out.println("- ID: " + ta.getId() + " | Intestatario: " + ta.getUtente().getNome() + " " + ta.getUtente().getCognome());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'ID della Tessera (o 0 per annullare):");
                    long idTessera = leggiNumeroSicuro(scanner);
                    if (idTessera == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Tessera t = em.find(Tessera.class, idTessera);

                    if (t == null) {
                        System.out.println("Errore: Tessera non trovata nel database.");
                    } else {
                        System.out.println("Tessera ID " + t.getId() + " intestata a: " + t.getUtente().getNome() + " " + t.getUtente().getCognome());
                        if (t.getDataScadenza().isBefore(java.time.LocalDate.now())) {
                            System.out.println("Stato: SCADUTA in data " + t.getDataScadenza());
                        } else {
                            System.out.println("Stato: VALIDA fino al " + t.getDataScadenza());
                        }
                    }
                    break;

                case "2":
                    System.out.println("\nELENCO PUNTI VENDITA DISPONIBILI:");
                    java.util.List<PuntoEmissione> puntiVenditaAdmin = em.createQuery("SELECT p FROM PuntoEmissione p", PuntoEmissione.class).getResultList();
                    for (PuntoEmissione pva : puntiVenditaAdmin) {
                        System.out.println("- ID: " + pva.getId() + " | Tipo: " + pva.getClass().getSimpleName());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'ID del Punto Vendita (o 0 per annullare):");
                    long idPunto = leggiNumeroSicuro(scanner);
                    if (idPunto == -1) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Long venduti = (Long) em.createQuery("SELECT COUNT(tv) FROM TitoloViaggio tv WHERE tv.puntoEmissione.id = :id")
                            .setParameter("id", idPunto)
                            .getSingleResult();
                    System.out.println("Il Punto Vendita indicato ha emesso un totale di " + venduti + " titoli.");
                    break;

                case "3":
                    /* Estrazione e stampa preliminare dei mezzi per agevolare l'input dell'amministratore */
                    System.out.println("\nELENCO MEZZI DISPONIBILI A SISTEMA:");
                    java.util.List<Mezzo> mezziVidimati = em.createQuery("SELECT m FROM Mezzo m", Mezzo.class).getResultList();
                    for (Mezzo m : mezziVidimati) {
                        System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'UUID del Mezzo per la verifica delle vidimazioni (o 0 per annullare):");
                    UUID idMezzoVid = leggiUUIDSicuro(scanner);
                    if (idMezzoVid == null) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Long vidimati = (Long) em.createQuery("SELECT COUNT(b) FROM Biglietto b WHERE b.mezzo.id = :id")
                            .setParameter("id", idMezzoVid)
                            .getSingleResult();

                    System.out.println("Statistica: Sul mezzo indicato sono stati vidimati " + vidimati + " biglietti in totale.");
                    break;

                case "4":
                    System.out.println("Sottomenu Manutenzione:");
                    System.out.println("1. Elenco mezzi in servizio");
                    System.out.println("2. Elenco mezzi in manutenzione");
                    System.out.println("3. Modificare lo stato di un mezzo");
                    System.out.print("Scelta: ");
                    String sub = scanner.nextLine();

                    if(sub.equals("1")) {
                        java.util.List<Mezzo> inServizio = statoDAO.findMezziInServizio();
                        System.out.println("MEZZI ATTUALMENTE IN SERVIZIO:");
                        inServizio.forEach(m -> System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId()));
                    } else if(sub.equals("2")) {
                        java.util.List<Mezzo> inManut = statoDAO.findMezziInManutenzione();
                        System.out.println("MEZZI ATTUALMENTE IN MANUTENZIONE:");
                        inManut.forEach(m -> System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId()));
                    } else if(sub.equals("3")) {
                        System.out.println("Inserire l'UUID del mezzo da aggiornare (o 0 per annullare):");
                        UUID uuidMezzo = leggiUUIDSicuro(scanner);
                        if (uuidMezzo == null) {
                            System.out.println("Operazione annullata. Ritorno al menu.");
                            break;
                        }

                        Mezzo mezzoStato = em.find(Mezzo.class, uuidMezzo);

                        if (mezzoStato == null) {
                            System.out.println("Errore: Mezzo non identificato.");
                        } else {
                            System.out.println("Selezionare il nuovo stato operativo: 1 per IN SERVIZIO, 2 per IN MANUTENZIONE");
                            String statoInput = scanner.nextLine();

                            if (statoInput.equals("1")) {
                                statoDAO.cambiaStato(mezzoStato, StatoVeicolo.IN_SERVIZIO, null);
                                System.out.println("Stato aggiornato: IN SERVIZIO.");
                            } else if (statoInput.equals("2")) {
                                System.out.println("Indicare la durata stimata della manutenzione in giorni (o 0 per annullare):");
                                long giorni = leggiNumeroSicuro(scanner);
                                if (giorni == -1) {
                                    System.out.println("Operazione annullata. Ritorno al menu.");
                                    break;
                                }

                                java.time.LocalDate finePrevista = java.time.LocalDate.now().plusDays(giorni);
                                statoDAO.cambiaStato(mezzoStato, StatoVeicolo.IN_MANUTENZIONE, finePrevista);
                                System.out.println("Stato aggiornato: IN MANUTENZIONE. Fine prevista: " + finePrevista);
                            } else {
                                System.out.println("Errore: Opzione non valida.");
                            }
                        }
                    } else {
                        System.out.println("Errore: Opzione non valida.");
                    }
                    break;

                case "5":
                    System.out.println("\nELENCO TRATTE DISPONIBILI A SISTEMA:");
                    java.util.List<Tratta> tratteAdmin = em.createQuery("SELECT t FROM Tratta t", Tratta.class).getResultList();
                    for (Tratta tra : tratteAdmin) {
                        System.out.println("- Percorso: " + tra.getZonaPartenza() + " -> " + tra.getCapolinea() + " | UUID: " + tra.getId());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'UUID della Tratta (o 0 per annullare):");
                    UUID idTratta = leggiUUIDSicuro(scanner);
                    if (idTratta == null) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    System.out.println("\nELENCO MEZZI DISPONIBILI A SISTEMA:");
                    java.util.List<Mezzo> listaMezzi = em.createQuery("SELECT m FROM Mezzo m", Mezzo.class).getResultList();
                    for (Mezzo m : listaMezzi) {
                        System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'UUID del Mezzo da assegnare alla percorrenza (o 0 per annullare):");
                    UUID idMezzoPercorrenza = leggiUUIDSicuro(scanner);
                    if (idMezzoPercorrenza == null) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    Tratta tratta = em.find(Tratta.class, idTratta);
                    Mezzo mezzoPercorrenza = em.find(Mezzo.class, idMezzoPercorrenza);

                    if (tratta == null || mezzoPercorrenza == null) {
                        System.out.println("Errore: Tratta o Mezzo non presenti a sistema.");
                    } else {
                        // Delega del salvataggio al PercorrenzaDAO
                        Percorrenza nuovaCorsa = new Percorrenza(mezzoPercorrenza, tratta, java.time.LocalDateTime.now());
                        percorrenzaDAO.save(nuovaCorsa);

                        System.out.println("\nRegistrazione percorrenza completata.");
                        System.out.println("Dettagli percorso: " + tratta.getZonaPartenza() + " -> " + tratta.getCapolinea());
                        System.out.println("Mezzo operativo: " + mezzoPercorrenza.getTipo());
                        System.out.println("Tempo di percorrenza stimato (condizioni ideali): " + tratta.getTempoBase() + " min");
                        System.out.println("Tempo di percorrenza effettivo (traffico/meteo inclusi): " + nuovaCorsa.getTempoEffettivo() + " min");
                    }
                    break;

                case "6":
                    System.out.println("\nELENCO MEZZI DISPONIBILI A SISTEMA:");
                    java.util.List<Mezzo> mezziStorico = em.createQuery("SELECT m FROM Mezzo m", Mezzo.class).getResultList();
                    for (Mezzo m : mezziStorico) {
                        System.out.println("- Tipo: " + m.getTipo() + " | UUID: " + m.getId());
                    }
                    System.out.println("----------------------------------------");

                    System.out.println("Inserire l'UUID del Mezzo per analizzare lo storico delle percorrenze (o 0 per annullare):");
                    UUID idMezzoStorico = leggiUUIDSicuro(scanner);
                    if (idMezzoStorico == null) {
                        System.out.println("Operazione annullata. Ritorno al menu.");
                        break;
                    }

                    java.util.List<Percorrenza> storico = percorrenzaDAO.findByMezzoId(idMezzoStorico.toString());

                    if (storico.isEmpty()) {
                        System.out.println("Nessuna percorrenza registrata a sistema per il mezzo selezionato.");
                    } else {
                        System.out.println("\nANALISI PERCORRENZE - MEZZO ID: " + idMezzoStorico);

                        for (Percorrenza p : storico) {

                            int tempoPrevisto = p.calcolaTempoPrevisto(p.getMezzo(), p.getTratta());
                            int tempoEffettivo = p.getTempoEffettivo();

                            System.out.println("- Tratta: " + p.getTratta().getZonaPartenza() + " -> " + p.getTratta().getCapolinea());
                            System.out.println("  Data Partenza: " + p.getDataPartenza());
                            System.out.println("  Tempo Previsto (Ideale):  " + tempoPrevisto + " min");
                            System.out.println("  Tempo Effettivo (Reale):  " + tempoEffettivo + " min");

                            /* Calcolo dello scostamento temporale per le statistiche */
                            if (tempoEffettivo > tempoPrevisto) {
                                System.out.println("  Stato: RITARDO di " + (tempoEffettivo - tempoPrevisto) + " min");
                            } else if (tempoEffettivo < tempoPrevisto) {
                                System.out.println("  Stato: ANTICIPO di " + (tempoPrevisto - tempoEffettivo) + " min");
                            } else {
                                System.out.println("  Stato: IN PERFETTO ORARIO");
                            }
                            System.out.println("  --------------------------------------");
                        }
                    }
                    break;

                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Errore: Opzione non valida.");
            }
        }
    }

    // ==========================================
    // METODI DI SICUREZZA PER LO SCANNER
    // ==========================================

    /**
     * Metodo di sicurezza per l'acquisizione di input numerici (Long).
     * Consente l'annullamento dell'operazione digitando '0'.
     */
    private static long leggiNumeroSicuro(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            // Valore sentinella per interrompere l'operazione
            if (input.equals("0")) {
                return -1;
            }

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.print("Formato errato. Inserire un numero valido o digitare '0' per annullare: ");
            }
        }
    }

    /**
     * Metodo di sicurezza per l'acquisizione di UUID validi.
     * Consente l'annullamento dell'operazione digitando '0'.
     */
    private static UUID leggiUUIDSicuro(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            // Valore sentinella per interrompere l'operazione
            if (input.equals("0")) {
                return null;
            }

            try {
                return UUID.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.print("Formato errato. Inserire un UUID valido o digitare '0' per annullare: ");
            }
        }
    }
}