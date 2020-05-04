package server;

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

        String ipLocal       = args[0];
        int puertoLocal      = Integer.parseInt(args[1]);
        String ipHoroscopo   = args[2];
        int puertoHoroscopo  = Integer.parseInt(args[3]);
        String ipPronostico  = args[4];
        int puertoPronostico = Integer.parseInt(args[5]);
        String url           = "rmi://" + ipLocal + ":" + puertoLocal + "/ServidorImplementacion";

        try {
            // Se instancian los servicios (stub)
            Servicios serv = new ServidorImplementacion(ipHoroscopo, puertoHoroscopo, ipPronostico, puertoPronostico);

            // Crea un registro en el host local que escuchara en el puerto pasado por parametro
            // Remplaza a rmiregistry
            LocateRegistry.createRegistry(puertoLocal);

            // Asociamos el objeto remoto (stub) al nombre del servicio y lo registramos
            Naming.bind(url, serv);
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
