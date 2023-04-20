package it.uninsubria.climatemonitoring.tgui;

import it.uninsubria.climatemonitoring.areaInteresse.AreaInteresse;
import it.uninsubria.climatemonitoring.dbref.DBInterface;
import it.uninsubria.climatemonitoring.operatore.Operatore;
import it.uninsubria.climatemonitoring.operatore.opeatoreAutorizzato.OperatoreAutorizzato;
import it.uninsubria.climatemonitoring.operatore.opeatoreRegistrato.OperatoreRegistrato;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalGUI {

    private final DBInterface dbInterface;
    private Operatore loggedOperatore;
    private boolean run;
    private boolean isLogged;

    /*TGui Strings*/
    private static final String welcomeText = "Benvenuto!\n" +
            "                        \n" +
            "                        Digitare 'cerca' per visualizzare le aree di interesse disponibili.\n" +
            "                        Digitare 'login' per effettuare il login (solo operatori registrati).\n" +
            "                        Digitare 'registrazione' per effettuare la registrazione all'applicazione\\s\n" +
            "                        (solo operatori autorizzati).\n" +
            "                        Digitare 'uscita' per terminare il programma.";
    private static final String areaRiservataWelcomeText = "Area Riservata\n" +
            "                        \n" +
            "                        Digitare 'aggiungi' per aggiungere un aree di interesse al centro di monitoraggio.\n" +
            "                        Digitare 'cerca' per visualizzare le aree di interesse disponibili.\n" +
            "                        Digitare 'logout' per effettuare il logout e tornare al menu' principale.\n" +
            "                        Digitare 'inserisci' per inserire i dati relativi ad una delle aree di interesse.\n" +
            "                        Digitare 'uscita' per terminare il programma.";
    private static final String cerca = "cerca";
    private static final String cercaAreaInteresse = "cerca area interesse";
    private static final String cercaCentroMonitoraggio = "cerca centro monitoraggio";
    private static final String aggiungiParametroClimatico = "aggiunta parametro climatico";
    private static final String aggiungiCentroMonitoraggio = "aggiunta centro monitoraggio";
    private static final String aggiungiAreaInteresse = "aggiunta area interesse";
    private static final String login = "login";
    private static final String registrazione = "registrazione";
    private static final String salva = "salva";
    private static final String uscita = "uscita";
    private static final String continua = "continua";
    private static final String quit = "quit";
    private static final String y = "y";
    private static final String n = "n";

    private static final String error_invalid_input = "Input non valido";

    private BufferedReader reader;

    public TerminalGUI(){
        this.dbInterface = new DBInterface();
        this.run = true;
        this.isLogged = false;

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public TerminalGUI(final DBInterface dbInterface){
        this.dbInterface = dbInterface;
        this.run = true;
        this.isLogged = false;

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Metodo dove girerà la gui fino al termine
     */
    public void run() throws IOException {
        while (true) {
            while (loggedOperatore == null) {
                System.out.println(welcomeText);
                switch (reader.readLine()) {
                    case "cerca", "c" -> cercaAreaInteresse();
                    case "login", "l" -> login();
                    case "registrazione", "r" -> registrazione();
                    case "uscita", "u" -> System.exit(0);
                }
            }

            while (loggedOperatore != null) {
                System.out.println("\nArea riservata - Centro di monitoraggio");
                System.out.println(areaRiservataWelcomeText);

                switch (reader.readLine()) {
                    case "aggiungi", "a" -> aggiungiAreaInteresse();
//                case "inserisci", "i" -> inserisciDatiParametri();
                    case "cerca", "c" -> cercaAreaInteresse();
                    case "logout", "l" -> loggedOperatore = null;
                    case "uscita", "u" -> System.exit(0);
                }
            }
        }
    }

    private void cercaAreaInteresse(){
        System.out.println("Digitare 'nome' per ricercare l'area di interesse per nome.\n" +
                "Digitare 'coordinate' per cercare l'area di interesse per coordinate geografiche.");

        try{
           switch(reader.readLine()){
               case "nome", "n" -> {
                   System.out.println("Digitare il nome dell'area di interesse:");
                   String nome = reader.readLine();
                   AreaInteresse cercata = dbInterface.getAreaInteresse(nome);
                   if(cercata != null) System.out.println(cercata);
                   else System.out.println("Area non trovata!");
               }
               case "coordinate", "c" -> {
                   System.out.println("Digitare la latitudine dell'area di interesse:");
                   double latitude = Double.parseDouble(reader.readLine());
                   System.out.println("Digitare la longitudine dell'area di interesse:");
                   double longitudine = Double.parseDouble(reader.readLine());
                   AreaInteresse cercata = dbInterface.getAreaInteresseWithCoordinates(latitude, longitudine);
                   if(cercata != null) System.out.println(cercata);
                   else System.out.println("Area non trovata!");
               }
               default -> cercaAreaInteresse();
           }
        }catch(IOException ioe){ioe.printStackTrace();}
    }


    private void aggiungiAreaInteresse(){

    }

    private void cercaCentroMonitoraggio(){

    }

    private void aggiungiCentroMonitoraggio(){

    }

    private void inserisciDatiParametroClimatico(){

    }

    private boolean login(){
        System.out.println("Login");
        String email = "";
        String password = "";
        BufferedReader terminalReader;
        try {
            System.out.println("Inserisci mail e password separate da spazio");
            terminalReader = new BufferedReader(new InputStreamReader(System.in));
            boolean cont = true;
            while(cont){
                String[] tmp = terminalReader.readLine().split(" ");
                Pair<String, String> credentials = new Pair<String, String>(tmp[0], tmp[1]);
                if(dbInterface.checkCredentials(credentials)){
                    this.isLogged = true;
                    this.loggedOperatore = dbInterface.getOperatoreRegistrato(credentials);
                    cont = false;
                }else{
                    System.out.println("Vuoi continuare? y/n");
                    String res = terminalReader.readLine();
                    if(res.equals(TerminalGUI.n)) cont = false;
                }
            }
        }catch(IOException ioe){ioe.printStackTrace();}
        return false;
    }

    private void registrazione(){
        System.out.println("Registrazione");
        try{
            reader = new BufferedReader(new InputStreamReader(System.in));
            boolean cont = true;
            while(cont){
                System.out.println("Inserisci codFisc operatore da registrare");
                String codFisc = reader.readLine();
                OperatoreAutorizzato op = dbInterface.getOperatoreAutorizzato(codFisc);
                if(op != null){
                    //Check fields
                    System.out.println("Codice fiscale corrispondente a persona autorizzata");
                    System.out.println("Inserisci userID: ");
                    String userID = reader.readLine();
                    System.out.println("Inserisci password: ");
                    String password = reader.readLine();
                    System.out.println("Inserisci centro di afferenza per l'operatore: ");
                    String centroID = reader.readLine();
                    if(dbInterface.checkCentroID(centroID)){
                        OperatoreRegistrato opReg = new OperatoreRegistrato(
                                op.getNome(), op.getCognome()
                                ,codFisc, op.getEmail(),
                                userID, password, centroID);
                        dbInterface.write(opReg);
                    }else{
                        System.out.println("Centro di monitoraggio inesistente!");
                    }
                }
                else{
                    System.out.println("Codice fiscale errato o inesistente");
                    System.out.println("Vuoi continuare? y/n");
                    String res =reader.readLine();
                    if(res.equals(TerminalGUI.n))
                        cont = false;
                }
            }
            //query db
            //If true -> log as op, save new op to file
            //if false -> error, start process from scratch
            //return
        }catch(IOException ioe){ioe.printStackTrace();}
    }

    private void readUserInput(){
        BufferedReader terminalReader = new BufferedReader(new InputStreamReader(System.in));
        try{
            switch(terminalReader.readLine()){
                case TerminalGUI.cercaAreaInteresse -> cercaAreaInteresse();
                case TerminalGUI.cercaCentroMonitoraggio -> cercaCentroMonitoraggio();
                case TerminalGUI.aggiungiAreaInteresse -> aggiungiAreaInteresse();
                case TerminalGUI.aggiungiCentroMonitoraggio -> aggiungiCentroMonitoraggio();
                case TerminalGUI.aggiungiParametroClimatico -> inserisciDatiParametroClimatico();
                case TerminalGUI.uscita -> System.exit(0);
                default -> throw new IllegalArgumentException(error_invalid_input);
            }
            terminalReader.close();
        }catch(IOException ioe){ioe.printStackTrace();}
    }

}
