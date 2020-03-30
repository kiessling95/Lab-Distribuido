package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorCentral {

    private final static int PORT = 10578;

    public static void main(String args[]) throws IOException {
        ServerSocket ss;
        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();

        String ipSH = "localhost";
        String ipSP = "localhost";

        if (args.length != 0) {
            ipSH = args[0];
            ipSP = args[1];
        }

        System.out.println("Inicializando servidor en el puerto " + PORT);
        System.out.println("IP del servidor de horoscopo: " + ipSH);
        System.out.println("IP del servidor de pronostico: " + ipSP);

        try {
            ss = new ServerSocket(PORT);
            int idSession = 0;
            while (true) {
                Socket socket = ss.accept();
                System.out.println("Nueva conexion entrante: " + socket);

                ((ServidorHilo) new ServidorHilo(socket, idSession, hm, ipSH, ipSP)).start();
                idSession++;
            }
        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}