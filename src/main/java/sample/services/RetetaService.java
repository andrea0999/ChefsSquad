package sample.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sample.entities.Reteta;

import sample.exceptions.RetetaAlreadyExistsException;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RetetaService {
    private static List<Reteta> retete = new ArrayList<>();
    private static  boolean dejaParcurs = false;

    public static List<Reteta> getRetete() {
        return retete;
    }

    public static Object readJsonRetete(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();

        return jsonParser.parse(reader);

    }

    public static void parcurgereListaRetete() throws Exception {
        if(!dejaParcurs){
            JSONArray listaRetete = (JSONArray) readJsonRetete("Retete.json");
            dejaParcurs=true;
            Iterator<JSONObject> iterator = listaRetete.iterator();
            while (iterator.hasNext()) {
                JSONObject retetaJson = iterator.next();
                Reteta r = new Reteta(retetaJson.get("nume").toString(), retetaJson.get("ingrediente").toString(), retetaJson.get("etapeCulinare").toString(), retetaJson.get("timpPreparare").toString());
                retete.add(r);
            }
        }
    }

    public static void writeJsonRetete(String filename) throws Exception {
        JSONArray listaRetete = new JSONArray();

        for(Reteta reteta : retete){
            JSONObject sampleObject = new JSONObject();

            sampleObject.put("nume", reteta.getNume());
            sampleObject.put("ingrediente", reteta.getIngrediente());
            sampleObject.put("etapeCulinare", reteta.getEtapeCulinare());
            sampleObject.put("timpPreparare", reteta.getTimpDePreparare());
            listaRetete.add(sampleObject);
        }

        Files.write(Paths.get(filename), listaRetete.toJSONString().getBytes());
    }


    public static void addReteta(String nume, String ingrediente, String etape, String timp) throws Exception {
        System.out.println("ReteteService->addReteta("+nume+", "+ingrediente+", "+etape+", "+timp+")");
        parcurgereListaRetete();
        checkRetetaDoesNotAlreadyExist(nume);
        Reteta r = new Reteta(nume,ingrediente,etape,timp);
        System.out.println(r.getNume() +" "+ r.getIngrediente() + " "+ r.getEtapeCulinare() + " "  +r.getTimpDePreparare());
        retete.add(r);

        writeJsonRetete("Retete.json");

    }



    private static void checkRetetaDoesNotAlreadyExist(String nume) throws RetetaAlreadyExistsException {
        System.out.println("RetetaService->checkRetetaDoesNotAlreadyExist()");
        for (Reteta reteta : retete) {
            if (nume.equals(reteta.getNume())) {
                System.out.println("same reteta");
                throw new RetetaAlreadyExistsException(nume);
            }
        }
    }

    public static List<Reteta>  getListaRetete() throws Exception {
        System.out.println("ReteteService->getListaRetete()");
        parcurgereListaRetete();
        return retete;

    }
}