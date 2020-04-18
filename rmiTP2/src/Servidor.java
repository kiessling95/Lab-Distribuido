import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Servidor {

    /**
     * @param args argumentos de la linea de comando
     * @param args[0] una direccion IP (localhost es valido)
     * @param args[1] un numero de puerto (debe tener asociado un servicio RMI Registry)
     */
    public static void main(String[] args) {
        // Si no se ingresan la cantidad de parametros correctos
        if (args.length < 6) {
            System.err.println("Uso: Servidor IPLocal PuertoLocal IPHoroscopo PuertoHoroscopo IPPronostico PuertoPronostico");
            return;
        }
        
        int puertoHoroscopo  = Integer.parseInt(args[3]);
        int puertoPronostico = Integer.parseInt(args[5]);

        try {
            Servicios serv = new ServidorImplementacion(args[2], puertoHoroscopo, args[4], puertoPronostico); // Se instancian los servicios
            
            LocateRegistry.createRegistry(Integer.parseInt(args[1]));
            
            // Se asocia una URL a la IP y puerto de los parametros
            Naming.rebind("rmi://" + args[0] + ":" + args[1] + "/ServidorImplementacion",serv);
        } catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Excepcion en Servidor:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
