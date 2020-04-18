import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ServidorHoroscopo extends UnicastRemoteObject implements ServiciosHoroscopo {

    protected ServidorHoroscopo() throws RemoteException {
        super();
    }

    /**
     * @param args argumentos de la linea de comando 
     * @param args[0] una direccion IP (localhost es valido)
     * @param args[1] un numero de puerto (debe tener asociado un servicio RMI Registry)
     */
    public static void main(String[] args) {
        if (args.length < 2) { // Si no se ingresan la cantidad de parametros correctos
            System.err.println("Uso: ServidorHoroscopo IPLocal PuertoLocal");
            return;
        }

        try {
			// Se instancian los servicios
			ServiciosHoroscopo serv = new ServidorHoroscopo(); 
            
            LocateRegistry.createRegistry(Integer.parseInt(args[1]));
            
			// Se asocia una URL a la IP y puerto de los parametros
            Naming.rebind("rmi://" + args[0] + ":" + args[1] + "/ServidorHoroscopo",serv); 

       } catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1); 
        } catch (Exception e) {
            System.err.println("Excepcion en Servidor:");
            e.printStackTrace();
            System.exit(1); 
        }
    }

    /**
	 * @param consulta es un signo del horoscopo
	 * @return una prediccion acorde al signo enviado por parametro
     */
    @Override
    public String consultarHoroscopo(String request) throws RemoteException {
		String respuesta = "Consulta recibida"; 

		System.out.println("Cliente> peticion [" + request + "]");

		//se procesa la peticion y se espera resultado
		respuesta = process(request); 


		System.out.println("Horoscopo> Resultado de peticion");
		System.out.println("Horoscopo> \"" + request + "\"");

		return respuesta; // Devuelve la predicción
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

