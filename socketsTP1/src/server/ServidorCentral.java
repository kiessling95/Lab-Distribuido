package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorCentral {

    public static void main(String args[]) throws IOException {
        ServerSocket ss;
        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();
        String spIP, shIP;
        int localPort, spPort, shPort;

        if (args.length == 5) {
            localPort = Integer.parseInt(args[0]);
            shIP      = args[1];
            shPort    = Integer.parseInt(args[2]);
            spIP      = args[3];
            shPort    = Integer.parseInt(args[4]);
        } else {
            System.out.println("Uso: ServidorCentral [puertoLocal servidorHoroscopo puertoHoroscopo servidorPronostico puertoHoroscopo]");

            return;
        }

        System.out.println("Inicializando servidor en el puerto " + localPort);
        System.out.println("IP del servidor de horoscopo: <" + shIP + "> en puerto: " + shPort);
        System.out.println("IP del servidor de pronostico: <" + spIP + "> en puerto: " + spPort);

        try {
            ss = new ServerSocket(localPort);
            int idSession = 0;
            while (true) {
                Socket socket = ss.accept();
                System.out.println("Nueva conexion entrante: " + socket);

                ((ServidorHilo) new ServidorHilo(socket, idSession, hm, shIP, spIP, shPort, spPort)).start();
                idSession++;
            }
        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
