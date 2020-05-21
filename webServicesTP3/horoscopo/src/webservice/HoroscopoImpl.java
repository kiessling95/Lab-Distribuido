package webservice;

import javax.jws.WebService;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@WebService(endpointInterface = "webservice.ServiciosHoroscopo")
public class HoroscopoImpl implements ServiciosHoroscopo {

    @Override
    public String consultarHoroscopo(String request) {
		String respuesta = "Consulta recibida"; 

		System.out.println("Horoscopo> Nueva peticion entrante: [" + request + "]");

		//Se procesa la peticion y se espera resultado
		respuesta = process(request); 

		System.out.println("Horoscopo> Resultado de peticion: [" + request + "] es: " + respuesta);

		return respuesta;
    }

    /**
     * Procesa peticion del cliente y retorna un resultado
     * @param request peticion del cliente
     * @return una prediccion para la entrada enviada
     */ 
    public String process(String request) {
		String result = "";
        ArrayList<String> phrasesList = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("Horoscopo.txt"))) {
            reader.lines().forEach(phrasesList::add);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Collections.addAll(phrasesList);
        Collections.shuffle(phrasesList);

        result = phrasesList.get(0);

        try {
            Thread.sleep((long) (Math.random()*(6000-1000)+1000));
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }

        return result;
	}
}

