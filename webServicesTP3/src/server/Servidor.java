package server;

import javax.xml.ws.Endpoint;

public class Servidor {

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

        
        Endpoint.publish("http://"+ipLocal+":"+puertoLocal+"/ws/Servidor", new ServidorImplementacion(ipHoroscopo, puertoHoroscopo, ipPronostico, puertoPronostico));
    }
}
