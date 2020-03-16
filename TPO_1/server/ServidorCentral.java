package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorCentral {

    public static void main(final String args[]) throws IOException {
        ServerSocket ss;
        ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<>();
        System.out.print("Inicializando servidor... ");
        try {
            ss = new ServerSocket(10578);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);

                ((ServidorHilo) new ServidorHilo(socket, idSession, hm)).start();
                idSession++;
            }
        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}