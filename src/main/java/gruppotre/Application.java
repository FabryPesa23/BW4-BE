package gruppotre;
import gruppotre.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        // ACCENSIONE DEL DATABASE
        System.out.println("Avvio del sistema... connessione al database in corso.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gruppotre");
        EntityManager em = emf.createEntityManager();

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
                    System.out.println("Inserisci l'ID del Punto Vendita (es. 1 per un Distributore):");
                    long idPunto = leggiNumeroSicuro(scanner);

                    // Peschiamo il punto vendita dal DB
                    PuntoEmissione punto = em.find(PuntoEmissione.class, idPunto);

                    if (punto != null) {
                        // Iniziamo la transazione per scrivere nel database
                        em.getTransaction().begin();
                        // Creiamo il biglietto con data e ora attuali (LocalDateTime)
                        Biglietto nuovoBiglietto = new Biglietto(java.time.LocalDateTime.now(), punto);
                        em.persist(nuovoBiglietto);
                        em.getTransaction().commit();

                        System.out.println("[SUCCESSO] Biglietto acquistato! Il tuo ID Biglietto è: " + nuovoBiglietto.getId());
                    } else {
                        System.out.println("ERRORE: Punto vendita inesistente. Controlla il database.");
                    }
                    break;

                case "2":
                    // VIDIMAZIONE BIGLIETTO
                    System.out.println("Inserisci l'ID del tuo Biglietto:");
                    long idBiglietto = leggiNumeroSicuro(scanner);
                    System.out.println("Inserisci l'ID del Mezzo su cui stai salendo:");
                    long idMezzo = leggiNumeroSicuro(scanner);

                    Biglietto bigliettoDaTimbrare = em.find(Biglietto.class, idBiglietto);
                    Mezzo mezzo = em.find(Mezzo.class, idMezzo);

                    if (bigliettoDaTimbrare == null) {
                        System.out.println("ERRORE: Biglietto non trovato.");
                    } else if (mezzo == null) {
                        System.out.println("ERRORE: Mezzo non trovato.");
                    } else if (bigliettoDaTimbrare.getDataVidimazione() != null) {
                        System.out.println("Furbetto! Questo biglietto è già stato timbrato il " + bigliettoDaTimbrare.getDataVidimazione());
                    } else {
                        em.getTransaction().begin();
                        // Timbra il biglietto registrando l'ora esatta e il mezzo
                        bigliettoDaTimbrare.setDataVidimazione(java.time.LocalDateTime.now());
                        bigliettoDaTimbrare.setMezzo(mezzo);
                        em.merge(bigliettoDaTimbrare);
                        em.getTransaction().commit();

                        System.out.println("[SUCCESSO] Biglietto vidimato correttamente. Buon viaggio!");
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
                    // Il costruttore di Tessera che avete fatto imposta la scadenza a 1 anno da oggi (LocalDate)
                    Tessera nuovaTessera = new Tessera(nuovoUtente);
                    nuovoUtente.setTessera(nuovaTessera);

                    // Salviamo l'utente (la tessera si salva in automatico grazie al CascadeType.ALL)
                    em.persist(nuovoUtente);
                    em.getTransaction().commit();

                    System.out.println("[SUCCESSO] Utente e Tessera creati! ID Tessera: " + nuovaTessera.getId() + " (Scade il: " + nuovaTessera.getDataScadenza() + ")");
                    break;

                case "4":
                    // SOTTOSCRIZIONE ABBONAMENTO
                    System.out.println("Inserisci l'ID della tua Tessera:");
                    long idTessera = leggiNumeroSicuro(scanner);
                    Tessera tessera = em.find(Tessera.class, idTessera);

                    if (tessera == null) {
                        System.out.println("ERRORE: Tessera inesistente. Fai la tessera al punto 3 prima di abbonarti.");
                        break;
                    }

                    // Controllo scadenza tessera
                    if (tessera.getDataScadenza().isBefore(java.time.LocalDate.now())) {
                        System.out.println("ERRORE: La tua tessera è scaduta. Non puoi abbonarti.");
                        break;
                    }

                    System.out.println("Inserisci l'ID del Punto Vendita:");
                    long idPuntoV = leggiNumeroSicuro(scanner);
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
                    Abbonamento nuovoAbbonamento = new Abbonamento(java.time.LocalDateTime.now(), puntoV, tipoEnum, tessera);
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


    // SOTTOMENU AMMINISTRATORE

    private static void gestisciMenuAdmin(Scanner scanner, EntityManager em) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- AREA AMMINISTRATORE ---");
            System.out.println("1. Controlla validità abbonamento");
            System.out.println("2. Visualizza statistiche vendite");
            System.out.println("3. Visualizza biglietti vidimati");
            System.out.println("4. Gestione manutenzione mezzi");
            System.out.println("0. Torna indietro");
            System.out.print("Scelta: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("[Admin 1] - Pronti per controllo abbonamento...");
                    break;
                case "2":
                    System.out.println("[Admin 2] - Pronti per statistiche vendite...");
                    break;
                case "3":
                    System.out.println("[Admin 3] - Pronti per statistiche vidimazioni...");
                    break;
                case "4":
                    System.out.println("[Admin 4] - Pronti per mandare mezzi in manutenzione...");
                    break;
                case "0":
                    inMenu = false; // Torna al menu principale
                    break;
                default:
                    System.out.println("ERRORE: Scelta non valida.");
            }
        }
    }


    // METODI DI SICUREZZA PER LO SCANNER


    // Questo metodo è uno scudo. Se l'utente digita una lettera quando gli chiedi un ID (numero),
    // invece di far esplodere il programma con un'eccezione, lo costringe a riprovare.
    private static long leggiNumeroSicuro(Scanner scanner) {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("ERRORE: Devi inserire un numero valido. Riprova: ");
            }
        }
    }
}