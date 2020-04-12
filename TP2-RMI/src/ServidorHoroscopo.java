import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.nio.file.*;
import java.rmi.*;
import java.util.*;

public class ServidorHoroscopo extends UnicastRemoteObject implements ServiciosHoroscopo {

    private static final long serialVersionUID = 1L;

    protected ServidorHoroscopo() throws RemoteException {

        super();

    }

    /**
    * @param args argumentos de la linea de comando 
    * @param args[0] una direccion IP (localhost es valido)
    * @param args[1] un numero de puerto (debe tener asociado un servicio RMI Registry)
    */

    public static void main(String[] args) {
        if (args.length != 2) { // Si no se ingresan la cantidad de parametros correctos
            System.err.println("Uso: Ingresar IP y Puerto");
            return;
        }
        /*if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager()); 
            System.setProperty("java.rmi.server.hostname", "localhost");
        }*/
        try {
			// Se instancian los servicios
			ServiciosHoroscopo serv = new ServidorHoroscopo(); 
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
	* @return una predicción acorde al signo enviado por parametro
    */

    @Override
    public String consultarHoroscopo(String request) throws RemoteException {
		String respuesta = "Consulta recibida"; 

		System.out.println("Cliente> petición [" + request + "]");

		//se procesa la peticion y se espera resultado
		respuesta = process(request); 


		System.out.println("Horoscopo> Resultado de petición");
		System.out.println("Horoscopo> \"" + request + "\"");

		return respuesta; // Devuelve la predicción
    }

    /**
     * procesa peticion del cliente y retorna resultado
     * @param request peticion del cliente
     * @return String
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

