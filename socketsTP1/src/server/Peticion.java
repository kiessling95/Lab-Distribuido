package server;

import java.io.*;
import java.util.concurrent.*;
import java.net.*;

public class Peticion implements Callable<String> {

    private String request;
    private String servidor;
    private int puerto;

    public Peticion(String cadena, String servidor, int puerto) {
        this.request = cadena;
        this.servidor = servidor;
        this.puerto = puerto;
    }

    @Override
    public String call() throws Exception {
        String response = "";
        Socket socket;

        try {
            socket = new Socket(this.servidor, this.puerto);

            //Para leer lo que envie el servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //para imprimir datos del servidor
            PrintStream output = new PrintStream(socket.getOutputStream());

            //manda signo al servidor
            output.println(request);
            //captura respuesta del servidor (una prediccion)
            response = input.readLine();
            //if( st != null ) System.out.println("Servidor> " + st );

            socket.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return response;
    }
}
