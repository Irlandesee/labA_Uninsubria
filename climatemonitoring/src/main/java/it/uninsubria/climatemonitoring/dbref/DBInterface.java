package it.uninsubria.climatemonitoring.dbref;

import it.uninsubria.climatemonitoring.areaInteresse.AreaInteresse;
import it.uninsubria.climatemonitoring.centroMonitoraggio.CentroMonitoraggio;
import it.uninsubria.climatemonitoring.city.City;
import it.uninsubria.climatemonitoring.climateParameters.ClimateParameter;
import it.uninsubria.climatemonitoring.dbref.readerDB.ReaderDB;
import it.uninsubria.climatemonitoring.dbref.writerDB.WriterDB;
import it.uninsubria.climatemonitoring.operatore.Operatore;
import it.uninsubria.climatemonitoring.operatore.opeatoreAutorizzato.OperatoreAutorizzato;
import it.uninsubria.climatemonitoring.operatore.opeatoreRegistrato.OperatoreRegistrato;
import javafx.util.Pair;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DBInterface {

    protected static final String geonamesAndCoordinatesDati = "data/geonames-and-coordinates.csv";
    protected static final String centroMonitoraggioDati = "data/centroMonitoraggio.dati";
    protected static final String coordinateMonitoraggioDati = "data/coordinateMonitoraggio.dati";
    protected static final String parametriClimaticiDati = "data/parametriClimatici.dati";
    protected static final String operatoriRegistratiDati = "data/operatoriRegistrati.dati";
    protected static final String operatoriAutorizzatiDati = "data/operatoriAutorizzati.dati";
    protected static final String areeInteresseDati = "data/areeInteresse.dati";

    protected static final String centroMonitoraggioFileHeader = "centroID;nomeCentro;via/piazza;numCivico;cap;comune;provincia;areaInteresse1,areaInteresseN";
    protected static final String geonamesFileHeader = "Geoname ID;Name;ASCII Name;Country Code;Country Name;Coordinates";
    protected static final String coordinateFileHeader = "geonameID;ASCII Name;CountryCode;Country;latitude,longitude";
    protected static final String operatoriRegistratiHeader = "userid;pwd;nomeOp;cognomeOp;codFiscale;mail;centroMonitoraggio";
    protected static final String operatoriAutorizzatiHeader = "cognomeOp;nomeOp;codFiscale;mailOp";
    protected static final String parametriClimaticiHeader = "centroID;areaInteresse;data;params1,paramN;note";
    protected static final String areeInteresseHeader = "areaID:denominazione:stato:latitude:longitude";

    private final File geonamesCoordinatesFile;
    private final File centroMonitoraggioFile;
    private final File coordinateMonitoraggioFile;

    private final File parametriClimaticiFile;
    private final File operatoriAutorizzatiFile;
    private final File operatoriRegistratiFile;
    private final File areeInteresseFile;
    private final boolean DEBUG = true;

    private HashMap<String, City> geonamesCache;
    private HashMap<String, CentroMonitoraggio> centroMonitoraggioCache;
    private HashMap<String, Operatore> operatoreRegistratoCache;
    private HashMap<String, Operatore> operatoreAutorizzatoCache;
    private HashMap<String, AreaInteresse> areeInteresseCache;

    private final ReaderDB readerREF;
    private final WriterDB writerREF;

    public DBInterface(){
        geonamesCoordinatesFile = new File(DBInterface.geonamesAndCoordinatesDati);
        centroMonitoraggioFile = new File(DBInterface.centroMonitoraggioDati);
        coordinateMonitoraggioFile = new File(DBInterface.coordinateMonitoraggioDati);
        parametriClimaticiFile = new File(DBInterface.parametriClimaticiDati);
        operatoriAutorizzatiFile = new File(DBInterface.operatoriAutorizzatiDati);
        operatoriRegistratiFile = new File(DBInterface.operatoriRegistratiDati);
        areeInteresseFile = new File(DBInterface.areeInteresseDati);

        this.readerREF = new ReaderDB(this);
        this.writerREF = new WriterDB(this);
        BufferedWriter bWriter;

        try {
            if (!geonamesCoordinatesFile.exists()) {
                boolean res = geonamesCoordinatesFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(geonamesCoordinatesFile));
                    bWriter.write(geonamesFileHeader);
                    bWriter.write("\n");
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        geonamesCoordinatesFile.getAbsolutePath());
            }
            if (!centroMonitoraggioFile.exists()) {
                boolean res = centroMonitoraggioFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(centroMonitoraggioFile));
                    bWriter.write(centroMonitoraggioFileHeader);
                    bWriter.write("\n");
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        centroMonitoraggioFile.getAbsolutePath());
            }
            if (!coordinateMonitoraggioFile.exists()) {
                boolean res = coordinateMonitoraggioFile.createNewFile();
                try {
                    bWriter = new BufferedWriter(new FileWriter(coordinateMonitoraggioFile));
                    bWriter.write(coordinateFileHeader);
                    bWriter.write("\n");
                    bWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                if (res && DEBUG) System.out.println("Created new file at: " +
                        coordinateMonitoraggioFile.getAbsolutePath());
            }
            if(!parametriClimaticiFile.exists()){
                boolean res = parametriClimaticiFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(parametriClimaticiFile));
                    bWriter.write(parametriClimaticiHeader);
                    bWriter.write("\n");
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        parametriClimaticiFile.getAbsolutePath());
            }
            if(!operatoriAutorizzatiFile.exists()){
                boolean res = operatoriAutorizzatiFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(operatoriAutorizzatiFile));
                    bWriter.write(operatoriAutorizzatiHeader);
                    bWriter.write("\n");
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        operatoriAutorizzatiFile.getAbsolutePath());
            }
            if(!operatoriRegistratiFile.exists()){
                boolean res = operatoriRegistratiFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(operatoriRegistratiFile));
                    bWriter.write(operatoriRegistratiHeader);
                    bWriter.write("\n");
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        operatoriRegistratiFile.getAbsolutePath());
            }
            if(!areeInteresseFile.exists()){
                boolean res = areeInteresseFile.createNewFile();
                try{
                    bWriter = new BufferedWriter(new FileWriter(areeInteresseFile));
                    bWriter.write(areeInteresseHeader);
                    bWriter.close();
                }catch(IOException ioe){ioe.printStackTrace();}
                if (res && DEBUG) System.out.println("Created new file at: " +
                        areeInteresseFile.getAbsolutePath());
            }
        }catch(IOException ioe){ioe.printStackTrace();}

    }

    public File getGeonamesCoordinatesFile() {
        return this.geonamesCoordinatesFile;
    }
    public File getCoordinateMonitoraggioFile() {
        return  this.coordinateMonitoraggioFile;
    }
    public File getCentroMonitoraggioFile(){return this.centroMonitoraggioFile;}
    public File getParametriClimaticiFile(){return this.parametriClimaticiFile;}
    public File getOperatoriAutorizzatiFile(){return this.operatoriAutorizzatiFile;}
    public File getOperatoriRegistratiFile(){return this.operatoriRegistratiFile;}
    public File getAreeInteresseFile(){return this.areeInteresseFile;}

    private HashMap<String, City> getGeonamesCache(){return this.geonamesCache;}
    private HashMap<String, CentroMonitoraggio> getCentroMonitoraggioCache(){return this.centroMonitoraggioCache;}
    private HashMap<String, Operatore> getOperatoreRegistratoCache(){return this.operatoreRegistratoCache;}
    private HashMap<String, Operatore> getOperatoreAutorizzatoCache(){return this.operatoreAutorizzatoCache;}
    private HashMap<String, AreaInteresse> getAreeInteresseCache(){return this.areeInteresseCache;}

    public boolean isDEBUG(){ return this.DEBUG;}
    public List<AreaInteresse> getAreeInteresseWithKey(List<String> keys){
        List<AreaInteresse> aree = new LinkedList<AreaInteresse>();
        keys.forEach((key) -> {
            if(areeInteresseCache.containsKey(key)) aree.add(areeInteresseCache.get(key));
        });
        return aree;
    }

    public void write(Object o){
        //TODO: in base alla stringa passata, chiama il metodo writer corrispondente
        boolean res;
        if(o instanceof AreaInteresse){
            res = this.writerREF.writeAreaInteresse((AreaInteresse) o);
        }else if(o instanceof CentroMonitoraggio){
            res = this.writerREF.writeCentroMonitoraggio((CentroMonitoraggio) o);
        }else if(o instanceof ClimateParameter){
            res = this.writerREF.writeParametroClimatico((ClimateParameter) o);
        }else if(o instanceof Operatore){
            if(o instanceof OperatoreAutorizzato){
                res = this.writerREF.writeOperatoreAutorizzato((OperatoreAutorizzato) o);
            }else if(o instanceof  OperatoreRegistrato){
                res = this.writerREF.writeOperatoreRegistrato((OperatoreRegistrato) o);
            }
        }
    }

    public void writeObjects(HashMap<String, Object> objs){
        //TODO: determine objects class at compile time and call appropriate method
    }

    public HashMap<String, ?> read(String objClass) {
        //TODO: in base alla stringa passata, chiama il metodo reader corrispondente }
        HashMap<String, ?> res = switch (objClass) {
            case "AreaInteresse" -> this.readerREF.readAreeInteresseFile();
            case "GeoName" -> this.readerREF.readGeonamesAndCoordinatesFile();
            case "OperatoreRegistrato" -> this.readerREF.readOperatoriRegistratiFile();
            case "OperatoreAutorizzato" -> this.readerREF.readOperatoriAutorizzatiFile();
            case "ClimateParameter" -> this.readerREF.readClimateParametersFile();
            case "CentroMonitoraggio" -> this.readerREF.readCentroMonitoraggiFile();
            default -> throw new IllegalArgumentException("invalid argument");
        };
        return res;
    }

    public <T> Pair<String, T>readLast(T objClass){
        //TODO:
        return null;
    }
}
