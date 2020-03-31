package server;

import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;

public class ServidorHoroscopo {

    private final static int PORT = 8000;

    public static void main(String[] args) {

        try {
            //Socket de servidor para esperar peticiones de la red
            ServerSocket serverSocket = new ServerSocket(PORT);
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Inicializando servidor horoscopo en el puerto " + PORT + " con IP " + ip + "\t[OK]");

            //Socket de cliente, en este caso el cliente sera el ServidorCentral
            Socket clientSocket;
            while (true) {
                // En espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                System.out.println("Horoscopo> Nueva conexion entrante: " + clientSocket);

                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida
                PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();
                System.out.println("ServidorCentral> Pidio la prediccion del signo [" + request +  "]");
                //se procesa la peticion y se espera resultado
                String strOutput = process(request);
                //Se imprime en consola "servidor"
                System.out.println("Horoscopo> La siguiente informacion sera devuelta: \"" + strOutput + "\"");
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println(strOutput);
                //cierra conexion
                clientSocket.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * procesa peticion del cliente y retorna resultado
     * @param request peticion del cliente
     * @return String
     */
    public static String process(String request) {
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
